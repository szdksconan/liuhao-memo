package com.liuhao.memo.mq.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuhao.memo.mq.dao.WareSkuDao;
import com.liuhao.memo.mq.po.WareSkuEntity;
import com.liuhao.memo.mq.vo.SkuLockVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WareSkuService extends ServiceImpl<WareSkuDao, WareSkuEntity> {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    StringRedisTemplate redisTemplate;


    private void lockStore(SkuLockVO skuLockVO){

        RLock lock = this.redissonClient.getLock("stock:" + skuLockVO.getSkuId());
        lock.lock();
        // 查询剩余库存够不够
        List<WareSkuEntity> wareSkuEntities = this.wareSkuDao.checkStore(skuLockVO.getSkuId(), skuLockVO.getCount());
        if (!CollectionUtils.isEmpty(wareSkuEntities)) {
            // 锁定库存信息
            Long id = wareSkuEntities.get(0).getId();
            this.wareSkuDao.lockStore(id, skuLockVO.getCount());
            skuLockVO.setWareSkuId(id);
            skuLockVO.setLock(true);
         } else {
            skuLockVO.setLock(false);
         }

         lock.unlock();
    }

    @Transactional
    public String checkAndLockStore(List<SkuLockVO> skuLockVOS) {

        if (CollectionUtils.isEmpty(skuLockVOS)) {
            return "没有选中的商品";
        }

        // 检验并锁定库存
        skuLockVOS.forEach(skuLockVO -> {
            lockStore(skuLockVO);
        });

        List<SkuLockVO> unLockSku = skuLockVOS.stream().filter(skuLockVO -> skuLockVO.getLock() == false).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unLockSku)) {
            // 解锁已锁定商品的库存
            List<SkuLockVO> lockSku = skuLockVOS.stream().filter(SkuLockVO::getLock).collect(Collectors.toList());
            lockSku.forEach(skuLockVO -> {
                this.wareSkuDao.unLockStore(skuLockVO.getWareSkuId(), skuLockVO.getCount());
            });
            // 提示锁定失败的商品
            List<Long> skuIds = unLockSku.stream().map(SkuLockVO::getSkuId).collect(Collectors.toList());
            return "下单失败，商品库存不足：" + skuIds.toString();
        }

        String orderToken = skuLockVOS.get(0).getOrderToken();
        this.redisTemplate.opsForValue().set("stock:lock" + orderToken, JSON.toJSONString(skuLockVOS));

        // 锁定成功，发送延时消息，定时解锁
       // this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "stock.ttl", orderToken);

        return null;
    }
}
