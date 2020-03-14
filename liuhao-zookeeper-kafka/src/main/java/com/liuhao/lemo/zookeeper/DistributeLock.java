package com.liuhao.lemo.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DistributeLock  {

    @Autowired
    private CuratorFramework zookeeperClient;
    private Lock lock = new ReentrantLock();


    public void lock(String serverPath) throws Exception {
        try {
            if (tryLock(serverPath)) {
                //业务处理
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            unlock(serverPath);
        }
    }


    /**
     * 获取锁
     * @param serverPath 表示锁代表的业务 和 粒度
     */
     boolean tryLock(String serverPath) throws Exception {

            //根节点的初始化放在构造函数里面不生效
            if (zookeeperClient.checkExists().forPath(serverPath) == null) {
                try {
                        lock.lock();
                        System.out.println("初始化根节点==========>" + serverPath);
                        //double check
                        if(zookeeperClient.checkExists().forPath(serverPath) == null){
                            zookeeperClient.create().creatingParentsIfNeeded().forPath(serverPath);
                        }

                    System.out.println("当前线程" + Thread.currentThread().getName() + "初始化根节点" + serverPath);
                } catch (Exception e) {

                }finally {
                    lock.unlock();
                }
            }

        String lockNum;
        try {
            //创建一个临时节点
            lockNum = this.zookeeperClient.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(serverPath + "/");
        } catch (Exception e) {
            return false;
        }

        if(lockNum == null){
            return false;
        }

        try {
            lockNum = lockNum.substring(lockNum.lastIndexOf("/")+1,lockNum.length());
            //判断当前节点是否是最小的 是最小的则获取锁
            List<String> childrens = this.zookeeperClient.getChildren().forPath(serverPath);
            //判断你自己的位置
            int index  = Collections.binarySearch(childrens,lockNum);
            if (index < 0) {
                throw new Exception("系统或网络异常，节点没有找到: " + lockNum);
            }
            if (index == 0) {
                System.out.println("当前线程获得锁" + lockNum);
                return true;
            }else{
                //取前一个节点
                Collections.sort(childrens);
                //如果自己没有获得锁，则要监听前一个节点
                String beforePath = serverPath + "/" + childrens.get(index - 1);
                waiForLock(beforePath);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }


    public void unlock(String serverPath) {
        try {
            zookeeperClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(serverPath);
            System.out.println("删除节点");
        } catch (Exception e) {
           //如果异常  网络异常就等待 zk 心跳自动检测后自动删除临时节点 其他异常看情况吧
        }
    }

    private void waiForLock(String beforePath){
        CountDownLatch cdl = new CountDownLatch(1);
        //创建监听器watch
        NodeCache nodeCache = new NodeCache(zookeeperClient,beforePath);
        try {
            nodeCache.start(true);
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    cdl.countDown();
                    System.out.println(beforePath + "节点监听事件触发，重新获得节点内容为：" + new String(nodeCache.getCurrentData().getData()));
                }
            });
        } catch (Exception e) {
        }
        //如果前一个节点还存在，则阻塞自己
        try {
            if (zookeeperClient.checkExists().forPath(beforePath) == null) {
                cdl.await();
            }
        } catch (Exception e) {
        }finally {
            //阻塞结束，说明自己是最小的节点，则取消watch，开始获取锁
            try {
                nodeCache.close();
            } catch (IOException e) {
            }
        }
    }







}
