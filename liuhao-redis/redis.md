# config
通过config get * 或者config get {name}获取配置信息，通过config set {name} {value} 设置参数

或者通过 redis.conf 修改配置

### 参数说明
1.daemonize no Redis 默认不是以守护进程的方式运行，可以通过该配置项修改，使用 yes 启用守护进程（Windows 不支持守护线程的配置为 no ）
2. pidfile /var/run/redis.pid 当 Redis 以守护进程方式运行时，Redis 默认会把 pid 写入 /var/run/redis.pid 文件，可以通过 pidfile 指定
3. bind 127.0.0.1 绑定的主机地址
4. port 6379
5. timeout 300 当客户端闲置多长时间后关闭连接，如果指定为 0，表示关闭该功能
6. loglevel notice 指定日志记录级别，Redis 总共支持四个级别：debug、verbose、notice、warning，默认为 notice
7. logfile stdout 日志记录方式，默认为标准输出，如果配置 Redis 为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给 /dev/null
8. databases 16 设置数据库的数量，默认数据库为0，可以使用SELECT 命令在连接上指定数据库id
9. save <seconds> <changes> 指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合,Redis 默认配置文件中提供了三个条件：分别表示 900 秒（15 分钟）内有 1 个更改，300 秒（5 分钟）内有 10 个更改以及 60 秒内有 10000 个更改。
    - save 900 1
    - save 300 10
    - save 60 10000
10. rdbcompression yes  指定存储至本地数据库时是否压缩数据，默认为 yes，Redis 采用 LZF 压缩，如果为了节省 CPU 时间，可以关闭该选项，但会导致数据库文件变的巨大
11. dbfilename dump.rdb  指定本地数据库文件名，默认值为 dump.rdb
12. dir ./	指定本地数据存放路径
13. slaveof <masterip> <masterport>	当被设置为salve时 启动时则会自动和mater进行数据同步
14. masterauth <master-password> master如果有密码 salve需要设置密码
15. requirepass foobared 设置 Redis 连接密码，如果配置了连接密码，客户端在连接 Redis 时需要通过 AUTH <password> 命令提供密码，默认关闭
16.  maxclients 128	设置同一时间最大客户端连接数，默认无限制，Redis 可以同时打开的客户端连接数为 Redis 进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis 会关闭新的连接并向客户端返回 max number of clients reached 错误信息
17.  maxmemory <bytes> 指定 Redis 最大内存限制，Redis 在启动时会把数据加载到内存中，达到最大内存后，Redis 会先尝试清除已到期或即将到期的 Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis 新的 vm 机制，会把 Key 存放内存，Value 会存放在 swap 区
18.  appendonly no	指定是否在每次更新操作后进行日志记录，Redis 在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis 本身同步数据文件是按上面 save 条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为 no 推荐不开启 可以选择设置哨兵模式 双写模式
19.  appendfilename appendonly.aof	指定更新日志文件名，默认为 appendonly.aof
20. appendfsync everysec 指定更新日志条件，共有 3 个可选值：
    - no：等操作系统缓存落盘
    - always：表示每次更新操作后手动调用 fsync() 将数据写到磁盘（慢，安全）
    - evertsec 每秒一次 默认
21. vm-enabled no 是否启用虚拟内存 即swap区  默认为no
22. vm-swap-file /tmp/redis.swap 虚拟内存文件路径，默认值为 /tmp/redis.swap，不可多个 Redis 实例共享
23. vm-max-memory 0 将所有大于 vm-max-memory 的数据存入虚拟内存，无论 vm-max-memory 设置多小，所有索引数据都是内存存储的(Redis 的索引数据 就是 keys)，也就是说，当 vm-max-memory 设置为 0 的时候，其实是所有 value 都存在于磁盘。默认值为 0
24. vm-page-size 32	Redis swap 文件分成了很多的 page，一个对象可以保存在多个 page 上面，但一个 page 上不能被多个对象共享，vm-page-size 是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page 大小最好设置为 32 或者 64bytes；如果存储很大大对象，则可以使用更大的 page，如果不确定，就使用默认值
25. vm-pages 134217728	设置 swap 文件中的 page 数量，由于页表（一种表示页面空闲或使用的 bitmap）是在放在内存中的，，在磁盘上每 8 个 pages 将消耗 1byte 的内存。
26. vm-max-threads 4	设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4
27. glueoutputbuf yes 设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启
28. hash-max-zipmap-entries 64
hash-max-zipmap-value 512 指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法
29. activerehashing yes	指定是否激活重置哈希，默认为开启
30. include /path/to/local.conf 指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件

## 结构

类型 | 简介 | 特征
---|---|---
string | 字符串,二进制安全 | 可以包含任何数据,比如jpg图片或者序列化的对象,一个键最大能存储512M
hash表 | 键值对集合,即编程语言中的Map类型 | 适合存储对象,并且可以像数据库中update一个属性一样只修改某一项属性值(Memcached中需要取出整个字符串反序列化成对象修改完再序列化存回去)
list | 数组+双向链表实现？| 增删快,提供了操作某一段元素的API
set | hashSet | 、添加、删除,查找的复杂度都是O(1)，为集合提供了求交集、并集、差集等操作
zset | sort set hash + 有序链表（维护了跳表索引？）|数据插入集合时,已经进行天然排序

## ps
Redis支持多个数据库，并且每个数据库的数据是隔离的不能共享，并且基于单机才有，如果是集群就没有数据库的概念。

Redis是一个字典结构的存储服务器，而实际上一个Redis实例提供了多个用来存储数据的字典，客户端可以指定将数据存储在哪个字典中。这与我们熟知的在一个关系数据库实例中可以创建多个数据库类似，所以可以将其中的每个字典都理解成一个独立的数据库。





