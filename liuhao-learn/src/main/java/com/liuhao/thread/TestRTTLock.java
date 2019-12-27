package com.liuhao.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 读写锁 status ！=0 且获取的是读锁 全部进入等待队列
 */
public class TestRTTLock {

    private ReentrantReadWriteLock reentrantReadWriteLock= new ReentrantReadWriteLock();
    private Lock readLock = reentrantReadWriteLock.readLock();
    private Lock writeLock = reentrantReadWriteLock.writeLock();
    private double x,y;

    public double read(){
        readLock.lock();
        try {
            return Math.sqrt(x*x+y*y);
        }finally {
            readLock.unlock();
        }
    }


    public void move(double deltaX, double deltaY) {
        writeLock.lock();
         try {
             x += deltaX;
             y += deltaY;
         } finally { //释放写锁
              writeLock.unlock();
         }
    }
    }

}
