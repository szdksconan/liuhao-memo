package com.liuhao.lemo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DistributeLock {

    @Autowired
    RedisUtil redisUtil;

    List<String> keys = new ArrayList();
    List<String> value = new ArrayList();
    StringBuilder  delLua = new StringBuilder();

    private DistributeLock(){
        initData();
        initLua();
    }

    /**
     * 利用set key value nx sec 设置锁 保持操作原子性
     * redis 老版本 可以用 lua脚本操作
     * 类似  if setnx()==true setExpire();
     *
     * 因为lua能摆正一个脚本运行的原子性
     *
     * 这里也可以通过lua脚本实现重入机制 value存申请锁的业务唯一标识 来判断是否重入
     * if get(key) == ARGV[1] return
     *
     */
    public boolean tryLock(String lockKey,String lockValue) throws InterruptedException {//value可以加入业务参数:比如加锁userId以便之后删除
        int tryCount = 2;
        boolean getLock = redisUtil.getRedisTemplate()
                .getRequiredConnectionFactory()
                .getConnection()
                .set(lockKey.getBytes(), lockValue.getBytes(), Expiration.seconds(10), RedisStringCommands.SetOption.ifAbsent());
        if(!getLock){
            //重试机制 、或业务处理
            if(tryCount--==0){
                return false;
            }
            Thread.sleep(50);
            tryLock(lockKey,lockValue);
        }
        if(tryCount<2){
            System.out.println("此时key为 "+lockKey+" 通过重试获取到锁 ");
        }else {
            System.out.println("此时key为 " + lockKey + "首次获取到锁 "+redisUtil.get(lockKey));
        }
        return getLock;
    }


    /**
     * 我们要确保删除之前设置的锁(删除之前最好加入value的比对，因为可能是别人之后申请的锁，如果value不相同则认为删除失败),如果删除失败，也就以为着之前的锁失效，比如：master节点挂了 sentinel让salve升为master节点，
     * 锁数据没有同步到salve，此时之前加的锁就等于失效。
     *
     * 删除失败，考虑在业务成面处理，看是回滚还是怎么，具体情况具体分析吧
     *
     *
     */
    public boolean unLock (String key,String value){
        DefaultRedisScript<Long> script = new DefaultRedisScript<Long>();
        script.setScriptText(delLua.toString());
        script.setResultType(Long.class);
        List<String> keys = new ArrayList<>();
        keys.add(key);
        Long result = redisUtil.getRedisTemplate().execute(script,keys,value);
        System.out.println("key为"+key+" value为"+value+"解锁状态："+ (result == 0 ? "失败":"成功"));
        return result == 0 ? false:true;

    }




    private void initData(){
        if(keys.size() == 0) {
            keys.add("liuhao");
            keys.add("liqian");
            keys.add("qinzhu");
            value.add("order1");
            value.add("order2");
            value.add("order3");
        }
    }

    private void initLua(){
        if(delLua.length()==0) {
            delLua.append("if redis.call('get',KEYS[1]) == ARGV[1] ")
                    .append(" then return redis.call('del',KEYS[1])")
                    .append(" else return 0 end");
        }

    }


    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
