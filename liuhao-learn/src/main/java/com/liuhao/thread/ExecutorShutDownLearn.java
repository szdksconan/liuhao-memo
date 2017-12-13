package com.liuhao.thread;

public class ExecutorShutDownLearn {


    public static void main(String[] args) {
        System.out.println("1111");
    }


    /**
     * 今天在看一个线程demo的时候 在线程都submit给线程池了后 线程池调用了shutdown()
     * shutdown()后线程池将变成shutdown状态，此时不接收新任务，但会处理完正在运行的 和 在阻塞队列中等待处理的任务。
     * shutdownNow()后线程池将变成stop状态，此时不接收新任务，不再处理在阻塞队列中等待的任务，还会尝试中断正在处理中的工作线程。
     *
     * 这里再强调下 execute 和 submit()
     * 两个方法都是 execute|submit(Runnable)，都是异步执行一个task，但是submit方法是有返回值的，它返回一个Future 对象，
     * 通过这个对象，可以检查这个Runnable实例是否执行完成。
     *
     * Future ：线程的并发模型 具体百度
     *
     *
     *
     * 这里主要测试 会干掉哪些线程
     */
    public void learnShutDwon(){


    }


}
