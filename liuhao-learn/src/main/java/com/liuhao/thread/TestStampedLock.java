package com.liuhao.thread;

import java.util.concurrent.locks.StampedLock;

public class TestStampedLock {

    private double x,y;
    private StampedLock stampedLock = new StampedLock();

    public double read(){
        //获取乐观锁 如果没有写锁在 则stamp 值不为0 此时共享变量会拷贝到栈中 这里和volatile不一样
        long stamp = stampedLock.tryOptimisticRead();
        //把最新的值记录
        double a=x,b=y;
        //此间没有线程操作过锁 则直接返回值
        if(!stampedLock.validate(stamp)){
            //不然就升级成悲观锁
            stamp = stampedLock.readLock();
            try{
                a = x;
                b = y;

            }finally {
                stampedLock.unlockRead(stamp);
            }


        }

        return Math.sqrt(a*a+b*b);
    }


    public void write(){

    }

}
