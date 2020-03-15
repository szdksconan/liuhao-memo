## 连接
远程：$ redis-cli -h host -p port -a password

本地：redis-cli 无密码状态

==去除乱码：redis-cli --raw==

## key常用命令


==object key encoding 查看数据类型==


```java
/**
* del key key1
*/

127.0.0.1:6379> del liuhao
1
127.0.0.1:6379> del liuhao liuhao1
0


/**
* dump key 返回序列化
*/
127.0.0.1:6379> dump liuhaostring 
刘浩	!DJ?/?0


/**
* exists key key1 是否存在 返回个数
*/

127.0.0.1:6379> exists liuhaoset
1
127.0.0.1:6379> exists liuhaoset liuhao liuhaozset liuhaolist
4
127.0.0.1:6379> exists liuhaoset liuhao liuhaozset liuhaolist liuhaomap
4


/**
* expire key sec 几秒后过期
* expireat key timestap 设置在 timestamp unix的 某个时间戳过期
*/

127.0.0.1:6379> get liuhaostring
刘浩
127.0.0.1:6379> expire liuhaostring 5
1
127.0.0.1:6379> get liuhaostring
刘浩
127.0.0.1:6379> get liuhaostring



127.0.0.1:6379> hmset liuhaohm liuhao value1
OK
127.0.0.1:6379> hmget liuhaohm liuhao
value1
127.0.0.1:6379> expire liuhaohm 5
1
127.0.0.1:6379> hmget liuhaohm liuhao



// pexpire key millsec 设置在多少毫秒后过期
// pexpireat key timestap 设置在某个unix时间戳过期 以毫秒为单位

127.0.0.1:6379> set liuhao 刘浩
OK
127.0.0.1:6379> get liuhao
刘浩
127.0.0.1:6379> pexpire liuhao 1000
1
127.0.0.1:6379> get liuhao



//keys keyname 可以用*匹配 如刘浩* *刘浩 *刘浩* 都支持

127.0.0.1:6379> keys *
liuhaozset
liuhaolist
liuhaoset
127.0.0.1:6379> keys liuhaolist
liuhaolist
127.0.0.1:6379> keys liuhao*
liuhaozset
liuhaolist
liuhaoset
127.0.0.1:6379> keys liuhaoz*
liuhaozset
127.0.0.1:6379> keys liuhaozset
liuhaozset
127.0.0.1:6379> keys *set
liuhaozset
liuhaoset
127.0.0.1:6379> keys *hao*
liuhaozset
liuhaolist
liuhaoset


//move key dbname 把key移动到指定数据库
127.0.0.1:6379> move liuhaolist 2
1
127.0.0.1:6379> keys *
liuhaozset
liuhaoset
127.0.0.1:6379> select 2
OK
127.0.0.1:6379[2]> keys *
liuhaolist
127.0.0.1:6379[2]> lrange liuhaolist 0 10
liqian
liqian
liuhao


//persist key 移除key的过期时间 
//pttl key 以毫秒为单位查看过期时间
//ttl key 查看过期时间

127.0.0.1:6379[2]> expire liuhaolist 100000
1
127.0.0.1:6379[2]> ttl liuhaolist 
99984
127.0.0.1:6379[2]> persist liuhaolist
1
127.0.0.1:6379[2]> ttl liuhaolist
-1



//	RANDOMKEY 从当前数据库中随机返回一个 key 



//rename keyname rename 修改key的名字 如果rename的key存在 则会覆盖之前的存在的key
127.0.0.1:6379> rename liuhaozset zset
OK
127.0.0.1:6379> keys *
zset
liuhaolist
liuhaoset
127.0.0.1:6379> 


//renamenx keyname rename 紧当rename 不存在时 才会修改成功
127.0.0.1:6379> keys *
zset
liuhaoset
127.0.0.1:6379> renamenx zset liuhaoset
0

// type key 返回key的 数据类型
127.0.0.1:6379> type zset
zset
127.0.0.1:6379> type liuhaoset
set
127.0.0.1:6379> type liuhaostring
string



```









## string



```java
// set key value   设置、获取值
// get key

127.0.0.1:6379[1]> set liuhao 16815700365
OK
127.0.0.1:6379[1]> get liuhao 
16815700365


// getrange key //获取截取字符串
127.0.0.1:6379[1]> getrange liuhao 4 5
57
127.0.0.1:6379[1]> getrange liuhao 4 9
570036


//getset key value //设置key的value 且 返回之前的value
27.0.0.1:6379[1]> set liqian 18782242147
OK
127.0.0.1:6379[1]> getset liqian 18982017223
18782242147
127.0.0.1:6379[1]> getset qinzhu 18802555555

127.0.0.1:6379[1]> get qinzhu
18802555555

//mget key..key1..key2 批量获取值
127.0.0.1:6379[1]> mget int liuhao qinzhu null liqian 
1
16815700365
18802555555

18982017223

//mset key value key1 value 批量设置
127.0.0.1:6379[1]> mset liuhao liuhao liqian liqian
OK
127.0.0.1:6379[1]> mget liuhao liqian
liuhao
liqian

//msetnx key value key1 value 批量设置值 且 所有key不存在才会成功
127.0.0.1:6379[1]> msetnx liuhao liuhaonx liqiannx liqian
0
127.0.0.1:6379[1]> msetnx liuhaonx liuhaonx liqiannx liqiannx
1
127.0.0.1:6379[1]> mget liuhaonx liqiannx
liuhaonx
liqiannx



//GETBIT key offset 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
//setbit key offset value 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。


//setex key second value 设置值和过期时间
//psetex key millisecond  value 设置值和过期时间 时间单位ms
27.0.0.1:6379[1]> setex liuhao8s 8 liuhao
OK
127.0.0.1:6379[1]> ttl liuhao8s
3
127.0.0.1:6379[1]> ttl liuhao8s
1
127.0.0.1:6379[1]> get liuhao8s

127.0.0.1:6379[1]


//setnx key value 设置值 紧当key不存在时才会设置成功 
127.0.0.1:6379[1]> setnx liuhao new
0
127.0.0.1:6379[1]> get liuhao
16815700365
127.0.0.1:6379[1]> setnx liuhaonew new
1
127.0.0.1:6379[1]> get liuhaonew
new


//setrange key offset value 从偏移值开始覆盖写值
127.0.0.1:6379[1]> setrange test 0 liuhao
6
127.0.0.1:6379[1]> get test
liuhao

//strlen key 返回数据长度 
127.0.0.1:6379[1]> strlen liuhao
11

//incr key 累加1返回当前值
//incrby key value 累加value返回当前值
127.0.0.1:6379[1]> setnx int 0
1
127.0.0.1:6379[1]> incr int 
1
127.0.0.1:6379[1]> get int 
1
127.0.0.1:6379[1]> incr int 
2
127.0.0.1:6379[1]> incr int 
3
127.0.0.1:6379[1]> get int 
3
127.0.0.1:6379[1]> incrby int 1999
2002
127.0.0.1:6379[1]> get int
2002


//incrbyfloat key value 累加浮点型 
127.0.0.1:6379[1]> set int 0
OK
127.0.0.1:6379[1]> incrbyfloat int 1.1
1.1


//decr 和 decrby value 累加1和累加指定值

127.0.0.1:6379[1]> set int 1999
OK
127.0.0.1:6379[1]> decr int 
1998
127.0.0.1:6379[1]> decr int 
1997
127.0.0.1:6379[1]> decrby int 1000
997
127.0.0.1:6379[1]> decrby int 10000
-9003

//append key value 尾部添加 数字也可以当字符串处理

127.0.0.1:6379[1]> append int 111
8
127.0.0.1:6379[1]> get int
-9003111
127.0.0.1:6379[1]> incrby int 10000000
996889
127.0.0.1:6379[1]> append int 1
7
127.0.0.1:6379[1]> get int
9968891



```



## hash

Redis hash 是一个 string 类型的 field 和 value 的映射表，hash 特别适合用于存储对象。

Redis 中每个 hash 可以存储 232 - 1 键值对（40多亿）


```java
// hmset mapname key value key value 设置k v值批量
// hset mapname key value 设置k v 
// hsetnx maoname key 不存在才设置
// hgetall mapname 获取map 下

127.0.0.1:6379[2]> hmset liuhao name liuhao age 18 dec 哈哈 address 成都
OK
127.0.0.1:6379[2]> hgetall liuhao
name
liuhao
age
18
dec
哈哈
address
成都
127.0.0.1:6379[2]> hsetnx liuhao age 20
0
127.0.0.1:6379[2]> hsetnx liuhao fal it
1
127.0.0.1:6379[2]> hvals liuhao
liuhao
20.1
哈哈
成都
liqian
it




//hexists key field 判断hash表 key存在否
127.0.0.1:6379[2]> hexists liuhao name
1

//hmget mapname key key1 .. 批量获取表里key的值
//hget mapname key 
127.0.0.1:6379[2]> hmget liuhao name age
liuhao
18
127.0.0.1:6379[2]> hget liuhao name
liuhao


//hincrby mapname key int 为key累加int的值
//hincrbyfloat mapname key folat 累加浮点型
127.0.0.1:6379[2]> hincrby liuhao age 1
19
127.0.0.1:6379[2]> hincrbyfloat liuhao age 1.1
20.1
127.0.0.1:6379[2]> 


//hkeys mapname 获取map下所有key
//hlen mapname 获取map长度
127.0.0.1:6379[2]> hkeys liuhao
name
age
dec
address
wife
127.0.0.1:6379[2]> hlen liuhao
5

//hvlas mapname 获取所有value
127.0.0.1:6379[2]> hvals liuhao
liuhao
20.1
哈哈
成都
liqian


//hscan mapname 游标位置 math 条件 count 数量
127.0.0.1:6379[2]> hscan phone 0 match li* count 10
0
liuhao
18615700365
liqian
18788242147
liuchang
1877778889



```



## list 列表
Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）

一个列表最多可以包含 232 - 1 个元素 (4294967295, 每个列表超过40亿个元素)。

底层实现方式：quicklist结构，每个quicklist有很多quicklistNode，Node是一个双向链表维护了prev和next指针，还有最重要其内部包含了一个ziplist，ziplist内部维护了其自身的大小和自身现在的offset以及尾部指针和一个entryX数组，每个entryX节点都包含了数据的长度类型和content，会根据数据的不同进行不同处理和压缩(二进制存放，比如用更小的空间去存一个较小的整数)，但是其本身具有数组的特性所以缺点也是插入和删除的迁移问题。这里quicklist还好因为
分段了ziplist所以其代价也会变小



```java



// lpush listname value value1头部添加元素
// rpush listname value value1尾部添加元素
// lrange listname 0 10 取0-10下标的值

127.0.0.1:6379[3]> lpush list:1 chengdu
1
127.0.0.1:6379[3]> lpush list:1 mianyang
2
127.0.0.1:6379[3]> lrange list:1 0 10
mianyang
chengdu


//lpushx listname value list存在才添加 value
//rpushx listname value list存在才在尾部添加 value
127.0.0.1:6379[3]> lpushx list 1
0
127.0.0.1:6379[3]> lpushx list:1 beijing
4
127.0.0.1:6379[3]> lrange list:1 0 11
beijing
mianyang
xindu
chengdu




//lindex listname 下标  通过索引获取值 -1表示最有一位
127.0.0.1:6379[3]> lindex list:1 0
mianyang
127.0.0.1:6379[3]> lindex list:1 -1
chengdu

//linsert listname after|before insertvalue value 在指定位置值的前后插入指定值
127.0.0.1:6379[3]> linsert list:1 after mianyang xindu
3
127.0.0.1:6379[3]> lrange list:1 0 11
mianyang
xindu
chengdu


//lpop listname 弹出第一个元素 
//rpop listname 弹出尾部一个元素
127.0.0.1:6379[3]> lrange list:1 0 11
beijing
mianyang
xindu
chengdu
127.0.0.1:6379[3]> lpop list:1
beijing
127.0.0.1:6379[3]> lrange list:1 0 11
mianyang
xindu
chengdu
127.0.0.1:6379[3]> rpop list:1
chengdu

//llen list:1 返回长度
127.0.0.1:6379[3]> llen list:1
3

//lset list:1 3 beijing 设置指定index的值 不能超出原有长度
127.0.0.1:6379[3]> lset list:1 3 beijing
ERR index out of range
127.0.0.1:6379[3]> lset list:1 2 beijing
OK

//lrem listname count value 移除指定value的count数量的值
//count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
//count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
//count = 0 : 移除表中所有与 VALUE 相等的值。
127.0.0.1:6379[3]> lrem list:1 1 xindu
1
127.0.0.1:6379[3]> lrange list:1 0 111
mianyang
beijing
127.0.0.1:6379[3]> llen list:1
2


//ltrim listname start end 裁剪且保留指定区间的值
127.0.0.1:6379[3]> lpush list shanghai hebei wuhan xicang yunnan mianyang chengdu xindu
8
127.0.0.1:6379[3]> lrange list 0 111
xindu
chengdu
mianyang
yunnan
xicang
wuhan
hebei
shanghai
127.0.0.1:6379[3]> ltrim list 5  7
OK
127.0.0.1:6379[3]> lrange list 0 1111
wuhan
hebei
shanghai

//rpoplpush source targetsource 如命令表意，尾部弹出新的list头部插入，没有则新建一个list
127.0.0.1:6379[3]> lrange list 0 1111
wuhan
hebei
shanghai
127.0.0.1:6379[3]> rpoplpush list newlist
shanghai

//blpop key1 [key2 ] timeout 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
//brpop key1 key2 timeout
//brpoplpush source target timeout 阻塞方式的尾部弹出插入新的list





```

## set 

Redis 的 Set 是 String 类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。

Redis 中集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。
 ```java
127.0.0.1:6379[4]> object encoding set
hashtable
```

```java

//sadd setname value value1  添加
//smembers setname 查看
//sismember setname value 是否是其中的元素
//scard setname 查看数量

127.0.0.1:6379[4]> scard set
4
127.0.0.1:6379[4]> smembers set
shanghai
beijing
chengdu
mianyang


//srem setname value value1 移除指定元素
127.0.0.1:6379[4]> smembers other
mianyang
shanghai
xian
127.0.0.1:6379[4]> srem other xian shanghai
2
127.0.0.1:6379[4]> smembers other
mianyang


//sdiff set targetset 去重
//sdiff newset set targetset 把重复的放入newset
127.0.0.1:6379[4]> sdiff setdiff set
xicang
luzhou
127.0.0.1:6379[4]> sdiff set setdiff
mianyang
beijing
127.0.0.1:6379[4]> sdiffstore other set setdiff
2
127.0.0.1:6379[4]> keys *
set
other
setdiff
127.0.0.1:6379[4]> smembers other
mianyang
beijing

//sinter set targetset 交集
//sinterstore newset set targetset 交集放入newset
127.0.0.1:6379[4]> sinter set setdiff
shanghai
chengdu
127.0.0.1:6379[4]> sinterstore otherinter set setdiff
2



//smove set targetset value 移动一个值到另外一个set
127.0.0.1:6379[4]> smembers set
shanghai
beijing
chengdu
mianyang
127.0.0.1:6379[4]> smembers other
mianyang
beijing
127.0.0.1:6379[4]> smove set other shanghai
1
127.0.0.1:6379[4]> smembers set
beijing
chengdu
mianyang
127.0.0.1:6379[4]> smembers other
shanghai
mianyang
beijing

//spop setname [count] 随机移除返回几个值 count默认为1 
127.0.0.1:6379[4]> spop other 3
xian
mianyang
beijing
127.0.0.1:6379[4]> smembers other
shanghai

//sunion setname targetsetname 取交集返回
//sunionsotre newset setname tragetsetname 取交集返回到newset里面
127.0.0.1:6379[4]> smembers other
mianyang
127.0.0.1:6379[4]> sunion other set
chengdu
beijing
mianyang
127.0.0.1:6379[4]> smembers set
beijing
chengdu
mianyang
127.0.0.1:6379[4]> sunionstore unionset set other
3
127.0.0.1:6379[4]> smembers unionset
mianyang
chengdu
beijing


//sscan key cursor [MATCH pattern] [COUNT count]迭代器 从cursor开始 计数count 匹配规则为 MATCH
127.0.0.1:6379[4]> sscan unionset 0 match c* count 10
0
chengdu


 


```


##zset (有序集合)


```java
127.0.0.1:6379[5]> object encoding zset
ziplist
```

```java
//zadd setname value score value1 score1 插入
//zrange setname start end 查询
//zrange setname start end withscores 查询数据和分数

127.0.0.1:6379[5]> zrange zset 0 10
riben
yibin
mianyang
chengdu
shanghai
127.0.0.1:6379[5]> zrange zset 0 10 withscores
riben
54
yibin
66
mianyang
100
chengdu
101
shanghai
888


//zrangebyscore setname start end [withscores] [limit offset count] //查询某个分数区间的元素 且支持分页
127.0.0.1:6379[5]> zrangebyscore zset 0 100 withscores
riben
54
yibin
66
mianyang
100
127.0.0.1:6379[5]> zrangebyscore zset 0 100 withscores limit 0 1
riben
54


//zcard setname 返回有序集合的成员数
//zcount setname startscore maxscore  返回区间分数的元素总数
127.0.0.1:6379[5]> zcard zset
5
127.0.0.1:6379[5]> zcount zset 0 100
3
127.0.0.1:6379[5]> zcount zset 0 200
4
127.0.0.1:6379[5]> zcount zset 0 1000
5

//zincrby setname inrscore value 在指定值的分数上加上分数
127.0.0.1:6379[5]> zincrby zset 16 riben
70
127.0.0.1:6379[5]> zrange zset 0 10 withscores
yibin
66
riben
70
mianyang
100
chengdu
101
shanghai
888

//zscore key name 获取指定值的分数
127.0.0.1:6379[5]> zscore zset riben 
70

//zrank key name 获取指定值分数的index 说白了就是分数排名
127.0.0.1:6379[5]> zrank zset riben
1
127.0.0.1:6379[5]> zrank zset mianyang
2
127.0.0.1:6379[5]> zrank zset shanghai
4

// zrem key name1 name2.. 删除指定值
// zremrangebyrank key start stop 删除分数指定排名的值
// zremrangebyscore key min max 删除指定分数区间的值
127.0.0.1:6379[5]> zrem zset mianyang
1
127.0.0.1:6379[5]> zrange zset 0 10
yibin
riben
chengdu
shanghai


// zrevrange key start stop [withscore] 顺序从高到低返回值
// zrevrangebysocre key max min [withscore] [limit offset count] ！！！注意这是max在前 从高到底的 顺序返回
127.0.0.1:6379[5]> zrevrange zset 0 100 withscores
shanghai
888
chengdu
101
riben
70
yibin
66

127.0.0.1:6379[5]> zrevrangebyscore zset 100 0  withscores
riben
70
yibin
66

//zrevrank setname name //查询指定name的 排名
127.0.0.1:6379[5]> zrevrank zset riben
2


//ZSCAN key cursor [MATCH pattern] [COUNT count]迭代有序集合中的元素（包括元素成员和元素分值）
//zlexcount key min max 在有序集合中计算指定[字典]区间内成员数量
//zunionstore destination numkeys key [key ...] 计算给定的一个或多个有序集的并集，并存储在新的 key 中




```


## 订阅/发布


```java 
//psubscibe  pattern [pattern ...]订阅一个或多个符合给定模式的频道。
//punsubscibe [pattern [pattern ...]]退订所有给定模式的频道。
//	unsubscribe [channel [channel ...]]指退订给定的频道。
127.0.0.1:6379> subscribe redischat
subscribe
redischat

//publish channelname "messge" //向一个主题发布消息
127.0.0.1:6379> publish redischat "liuhao"
(integer) 1

//pubsub channels  [argument [argument ...]] 查看主题状态
127.0.0.1:6379> pubsub channels
1) "redischat"








```



## redis 事务

一个事务从开始到执行会经历以下三个阶段：

开始事务。
命令入队。
执行事务。

它先以 MULTI 开始一个事务， 然后将多个命令入队到事务中， 最后由 EXEC 命令触发事务， 一并执行事务中的所有命令

单个 Redis 命令的执行是原子性的，但 Redis 没有在事务上增加任何维持原子性的机制，所以 Redis 事务的执行并不是原子性的。

==事务可以理解为一个打包的批量执行脚本，但批量指令并非原子化的操作，中间某条指令的失败不会导致前面已做指令的回滚，也不会造成后续的指令不做。==

```java 




1	DISCARD
取消事务，放弃执行事务块内的所有命令。
2	EXEC
执行所有事务块内的命令。
3	MULTI
标记一个事务块的开始。
4	UNWATCH
取消 WATCH 命令对所有 key 的监视。
5	WATCH key [key ...]
监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断。

```



## 脚本
==EVAL script numkeys key [key ...] arg [arg ...]==

==numkeys:key的数量 没有写0==

==key：lua脚本定义格式为KEYS[1] KEYS[2] 从1开始 必须为大写==

==arg: lua脚本定义格式为ARGV[1] ARGV[2] 从1开始 必须为大写==



```java

//EVAL script numkeys key [key ...] arg [arg ...] 执行脚本

127.0.0.1:6379> eval "return redis.call('HGET',KEYS[1],KEYS[2])" 2 phone liuhao
18615700365

27.0.0.1:6379> eval "return redis.call('hgetall','phone')" 0 
liuhao
18615700365
qinzhu
13332882432
liuchang
13346644455


//script load "脚本" 把脚本缓存在服务器 返回一个sha1校验码
//evalsha1 sha1 numbe numkeys key [key ...] arg [arg ...] 通过sha1校验码执行脚本

127.0.0.1:6379> script load "return redis.call('hgetall','phone')"
8d05585b590fb8c9bc3f51b8d66af8fdfcbc495c
127.0.0.1:6379> evalsha 8d05585b590fb8c9bc3f51b8d66af8fdfcbc495c 0
liuhao
18615700365
qinzhu
13332882432
liuchang
13346644455

127.0.0.1:6379> script load "return redis.call('HGET',KEYS[1],KEYS[2])"
a4100d1d7051615bcea95168a9e7d97876a28c45
127.0.0.1:6379> evalsha a4100d1d7051615bcea95168a9e7d97876a28c45 2 phone liuhao
18615700365


//script exists sha1 sha2...判断脚本是否存在缓存
127.0.0.1:6379> script exists a4100d1d7051615bcea95168a9e7d97876a28c45
1
127.0.0.1:6379> script exists 111
0


//script flush 清楚缓存脚本
127.0.0.1:6379> SCRIPT FLUSH
OK
127.0.0.1:6379> script exists a4100d1d7051615bcea95168a9e7d97876a28c45
0

//SCRIPT KILL 杀死正在运行的lua脚本




```




## 服务器相关命令  参考



```java 

1	BGREWRITEAOF
异步执行一个 AOF（AppendOnly File） 文件重写操作
2	BGSAVE
在后台异步保存当前数据库的数据到磁盘
3	CLIENT KILL [ip:port] [ID client-id]
关闭客户端连接
4	CLIENT LIST
获取连接到服务器的客户端连接列表
5	CLIENT GETNAME
获取连接的名称
6	CLIENT PAUSE timeout
在指定时间内终止运行来自客户端的命令
7	CLIENT SETNAME connection-name
设置当前连接的名称
8	CLUSTER SLOTS
获取集群节点的映射数组
9	COMMAND
获取 Redis 命令详情数组
10	COMMAND COUNT
获取 Redis 命令总数
11	COMMAND GETKEYS
获取给定命令的所有键
12	TIME
返回当前服务器时间
13	COMMAND INFO command-name [command-name ...]
获取指定 Redis 命令描述的数组
14	CONFIG GET parameter
获取指定配置参数的值
15	CONFIG REWRITE
对启动 Redis 服务器时所指定的 redis.conf 配置文件进行改写
16	CONFIG SET parameter value
修改 redis 配置参数，无需重启
17	CONFIG RESETSTAT
重置 INFO 命令中的某些统计数据
18	DBSIZE
返回当前数据库的 key 的数量
19	DEBUG OBJECT key
获取 key 的调试信息
20	DEBUG SEGFAULT
让 Redis 服务崩溃
21	FLUSHALL
删除所有数据库的所有key
22	FLUSHDB
删除当前数据库的所有key
23	INFO [section]
获取 Redis 服务器的各种信息和统计数值
24	LASTSAVE
返回最近一次 Redis 成功将数据保存到磁盘上的时间，以 UNIX 时间戳格式表示
25	MONITOR
实时打印出 Redis 服务器接收到的命令，调试用
26	ROLE
返回主从实例所属的角色
27	SAVE
同步保存数据到硬盘
28	SHUTDOWN [NOSAVE] [SAVE]
异步保存数据到硬盘，并关闭服务器
29	SLAVEOF host port
将当前服务器转变为指定服务器的从属服务器(slave server)
30	SLOWLOG subcommand [argument]
管理 redis 的慢日志
31	SYNC
用于复制功能(replication)的内部命令





```


## 安全认证

```java

//config set requirepass password 设置密码
//config get requirepass password 获取密码
//auth password 认证密码  认证后才能 操作redis

127.0.0.1:6379> CONFIG get requirepass
requirepass
123456

127.0.0.1:6379> auth 123456
OK

```


## 性能测试 记录下 没实践

Redis 性能测试是通过同时执行多个命令实现的。提供了redis-benchmark工具

```java

redis 性能测试工具可选参数如下所示：

序号	选项	描述	默认值
1	-h	指定服务器主机名	127.0.0.1
2	-p	指定服务器端口	6379
3	-s	指定服务器 socket	
4	-c	指定并发连接数	50
5	-n	指定请求数	10000
6	-d	以字节的形式指定 SET/GET 值的数据大小	2
7	-k	1=keep alive 0=reconnect	1
8	-r	SET/GET/INCR 使用随机 key, SADD 使用随机值	
9	-P	通过管道传输 <numreq> 请求	1
10	-q	强制退出 redis。仅显示 query/sec 值	
11	--csv	以 CSV 格式输出	
12	-l	生成循环，永久执行测试	
13	-t	仅运行以逗号分隔的测试命令列表。	
14	-I	Idle 模式。仅打开 N 个 idle 连接并等待。

$ redis-benchmark -h 127.0.0.1 -p 6379 -t set,lpush -n 10000 -q

SET: 146198.83 requests per second
LPUSH: 145560.41 requests per second
以上实例中主机为 127.0.0.1，端口号为 6379，执行的命令为 set,lpush，请求数为 10000，通过 -q 参数让结果只显示每秒执行的请求数。

```

