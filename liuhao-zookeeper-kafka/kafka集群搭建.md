
## 体系
Kafka体系架构=M个producer +N个broker +K个consumer+ZK集群

producer:生产者
Broker：服务代理节点，Kafka服务实例。
n个组成一个Kafka集群，通常一台机器部署一个Kafka实例，一个实例挂了其他实例仍可以使用，体现了高可用

consumer：消费者
消费topic 的消息， 一个topic 可以让若干个consumer消费，若干个consumer组成一个 consumer group ，一条消息只能被consumer group 中一个consumer消费，若干个partition 被若干个consumer 同时消费，达到消费者高吞吐量

topic ：主题

partition： 一个topic 可以拥有若干个partition（从 0 开始标识partition ），分布在不同的broker 上， 实现发布与订阅时负载均衡。producer 通过自定义的规则将消息发送到对应topic 下某个partition，以offset标识一条消息在一个partition的唯一性。
一个partition拥有多个replica，提高容灾能力。
replica 包含两种类型：leader 副本、follower副本，
leader副本负责读写请求，follower 副本负责同步leader副本消息，通过副本选举实现故障转移。
partition在机器磁盘上以log 体现，采用顺序追加日志的方式添加新消息、实现高吞吐量


## 安装 

### 安装ZK(本来自己用docker安装过一次集群，但是docker集群内部网络构建比较麻烦，自己也不是专业的运维，且配置文件也需要自己copy所以选择了手动安装，也不推荐homebrew安装，下得太慢了)
1. 需要java环境
2. 下载zookeeper： https://mirrors.cnnic.cn/apache/zookeeper/
3. 环境变量:
 ```shell
 export ZOOKEEPER_HOME = /Users/liuhao/Downloads/codingSoft/zk/zookeeper-3.4.14
export KAFKA = /Users/liuhao/Downloads/codingSoft/kafka/kafka_2.12-2.3.1
export PATH=$M2:$PATH:$ZOOKEEPER_HOME/binL:$KAFKA/bin

 ```
 
4. 配置 ：
    - dataDir=/Users/liuhao/Downloads/codingSoft/zk/data/1
存快照文件的目录，默认情况下， 事务日志也会存储在该目
dataDir录上。
    - 由于事务日志 的写性能直接影响 ZooKeeper 性能，因此 建议同时配置参数 dataLogDir
dataLogDir=/Users/liuhao/Downloads/codingSoft/zk/logs/1。
 
 
 
    
4. 集群：
    - 进入其中一台机器的Zookeeper安装路径conf，添加
        
        server.1=server-1:2888:3888

        server.2=server-2:2888:3888
        
        server.3=server-3:2888:3888
        
        端口号2888表示该服务器与集群中leader交换信息的端口，默认为2888， 3888表示选举时服务器相互通信的端口。
        
    - 接着在${dataDir}路径下创建一个myid文件，myid存放的值就是服务器的编号，即对应上面的1、2、3。ZooKeeper在启动时会读取 myid文件 中的值与 zoo.cfg文件中的配置信息进行比较， 以确定是哪台服务器。
    
    - 接着在${dataDir}路径下创建一个myid文件，myid存放的值就是服务器的编号，即对应上面的1、2、3。ZooKeeper在启动时会读取 myid文件 中的值与 zoo.cfg(现在有些版本默认的simpleZoo.cfg需要自己copy一份改成zoo.cfg)文件中的配置信息进行比较， 以确定是哪台服务器。
    
    - zkServer.sh start 启动
    
      zkServer.sh status 查看状态


### 安装kafka kafka_2.12-2.3.1

1. 下载地址  http://kafka.apache.org/downloads 选择Scala打包好的版本
2. 环境变量配置 ：参考zookeeper
3. 配置: 修改$KAFKA_HOME/config 目录下的 server.prope叫es文件，为了便于后 续集群环境搭建的配置， 需要保证同一个集群下 broker.id要唯一，因此这里手动配置 broker.id, 直接保持与ZooKeeper的myid值一致， 同时配置日志存储路径。server.properties修改的配置 如下 :
broker.id=l #指定代理的 id
log.dirs=/Users/liuhao/Downloads/codingSoft/kafka/logs
 #指定 Log 存储路径
zookeeper . connect=server-1:2181 , server - 2:2181 , server-3:2181
4. 用bin文件下的shell启动 kafka-server-start.sh -daemon ../config/server.properties
5. 执行 jps命令查看 Java进程，此时进程信息至少包括以下几项:
    - 14999 QuorumPeerMain zk进群的入口进程
    - 15906 Kafka
6.本地伪集群：copy server.properties 出来2份 依次修改配置：
    - broker.id=20
    - listeners=PLAINTEXT://127.0.0.1:9095
    - log.dirs=/Users/liuhao/Downloads/codingSoft/kafka/logs/3
    - 依次启动 2 3配置文件 kafka-server-start.sh -daemon ../config/server2.peoperties
```java
    
liuhao@liuhaodeMacBook-Pro bin % sh kafka-server-start.sh -daemon ../config/server.properties
liuhao@liuhaodeMacBook-Pro bin % jps
46720 Jps
46049 RemoteMavenServer
46055 Launcher
36956 
45116 QuorumPeerMain
46719 Kafka
liuhao@liuhaodeMacBook-Pro bin % sh kafka-server-start.sh -daemon ../server_2.properties
liuhao@liuhaodeMacBook-Pro bin % jps
46049 RemoteMavenServer
47011 Kafka
47012 Jps
46055 Launcher
36956 
45116 QuorumPeerMain
46719 Kafka
liuhao@liuhaodeMacBook-Pro bin % sh kafka-server-start.sh -daemon ../server_3.properties
liuhao@liuhaodeMacBook-Pro bin % jps                                                    
46049 RemoteMavenServer
47011 Kafka
46055 Launcher
47306 Kafka
47307 Jps
36956 
45116 QuorumPeerMain
46719 Kafka

```
### 测试 ==kafka的副本只提供备份功能 测试请找准leader==

```
//创建一个topic
liuhao@liuhaodeMacBook-Pro bin % sh kafka-topics.sh --create --zookeeper 127.0.0.1:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic
Created topic my-replicated-topic.

//查看集群状况 isr三个实例 isr是指在 in syn relication 就是指同步圈子的实例，这个判定时间可以设置

liuhao@liuhaodeMacBook-Pro bin % sh kafka-topics.sh --describe --zookeeper 127.0.0.1:2181 --topic my-replicated-topic
Topic:my-replicated-topic	PartitionCount:1	ReplicationFactor:3	Configs:
	Topic: my-replicated-topic	Partition: 0	Leader: 10	Replicas: 10,0,20	Isr: 10,0,20
	
//producer 几条消息
liuhao@liuhaodeMacBook-Pro bin % sh kafka-console-producer.sh --broker-list 127.0.0.1:9093 --topic my-replicated-topic

//然后consumer 
liuhao@liuhaodeMacBook-Pro bin % sh kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9093 --from-beginning --topic my-replicated-topic
1
2

//最后kill 掉leader  测试主从切换
liuhao@liuhaodeMacBook-Pro bin % kill -9 47011
liuhao@liuhaodeMacBook-Pro bin % sh kafka-topics.sh --describe --zookeeper 127.0.0.1:2181 --topic my-replicated-topic
Topic:my-replicated-topic	PartitionCount:1	ReplicationFactor:3	Configs:
	Topic: my-replicated-topic	Partition: 0	Leader: 0	Replicas: 10,0,20	Isr: 0,20
	
//再次查看消息
liuhao@liuhaodeMacBook-Pro bin % sh kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9094 --from-beginning --topic my-replicated-topic
1
2

//重启 kill掉的leader 再查看集群情况
liuhao@liuhaodeMacBook-Pro bin % sh kafka-topics.sh --describe --zookeeper 127.0.0.1:2181 --topic my-replicated-topic                       
Topic:my-replicated-topic	PartitionCount:1	ReplicationFactor:3	Configs:
	Topic: my-replicated-topic	Partition: 0	Leader: 0	Replicas: 10,0,20	Isr: 0,20,10


```




