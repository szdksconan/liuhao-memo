package com.liuhao.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorShutDownLearn {


    public static void main(String[] args) {
        ExecutorShutDownLearn executorShutDownLearn =new ExecutorShutDownLearn();
        //executorShutDownLearn.learnShutDwon();
        //executorShutDownLearn.leaenShutDownNow();
        //executorShutDownLearn.learnDemo();
        executorShutDownLearn.learnShutDwon_1();
    }


    /**
     网上看到一个更通俗理解的例子

     虽然使用ExecutorService可以让线程处理变的很简单，
     可是有没有人觉得在结束线程运行时候只调用shutdown方法就可以了？
     实际上，只调用shutdown方法的是不够的。
     我们用学校的老师和学生的关系来说明这个问题。

     shutdown只是起到通知的作用

     我们来假设如下场景：
     学校里在课上老师出了一些问题安排全班同学进行解答并对学生说“开问题解答完毕后请举手示意！”
     如果有学生解答完毕后会举手对老师说“老师我做完了！”，如果大家都解题完毕后上课结束。
     上面的场景对应于ExecutorService里的方法的话是下面的样子。
     老师: ExecutorService
     学生: ExecutorService里的线程
     问题: 通过参数传递给ExecutorService.execute的任务(Runnable)
     授课: main线程
     学校: Java进程

     “问题解答完毕后请举手示意！”是shutdown方法。“老师我做完了！”是各个任务（Runnable）的运行结束。
     所有的任务（Runnable）都结束了的话main线程(授课)也结束了。
     在这里，我们假设试卷中有难度较大的问题，当然学生解答较难的问题也会比较花时间。
     在上面的场景中老师除了shutdown方法之外什么也做不了，只能呆呆得等着学生们说，“老师我做完了！”之后才可以有下一步动作。
     这都是因为shutdown方法只是用来通知的方法。
     这时如果即使授课时间结束（main线程结束），学校也不能放学（Java进程结束），因为学生们还在解题中呢。这个时候如果你是老师你会怎么做？
     一般的情况肯定是经过一定的时间在授课快要结束的时候，如果还有人没有解答出来的话，或者公布给大家解题方法，
     或者作为课后习题让学生回去继续思考，然后结束上课对不对！

     定好下课时间后等待结束

     如果经过了一定的时间任务（Runnable）还不结束的时候我们可以通过中止任务（Runnable）的执行，以防止一直等待任务的结束。
     awaitTermination方法正是可以实现这个中止作用的角色。
     具体的使用方法是，在shutdown方法调用后，接着调用awaitTermination方法。这时只需要等待awaitTermination方法里第一个参数指定的时间。
     如果在指定的时间内所有的任务都结束的时候，返回true，反之返回false。返回false意味着课程结束的时候还有题目没有解答出来的学生。
     通过shutdownNow方法，我们可以作为老师向同学发出“没有解答出来的同学明天给出解答”的命令后结束授课。
     shutdownNow方法的作用是向所有执行中的线程发出interrupted以中止线程的运行。这时，各个线程会抛出InterruptedException异常（前提是
     线程中运行了sleep等会抛出异常的方法）
     所以正确的中止线程的方法如下面代码：
     *
     */
    public void learnDemo(){
        ExecutorService pool = Executors.newFixedThreadPool(5);
        final long waitTime = 8 * 1000;
        final long awaitTime = 5 * 1000;

        Runnable task1 = new Runnable(){
            public void run(){
                try {
                    System.out.println("task1 start");
                    Thread.sleep(waitTime);
                    System.out.println("task1 end");
                } catch (InterruptedException e) {
                    System.out.println("task1 interrupted: " + e);
                }
            }
        };

        Runnable task2 = new Runnable(){
            public void run(){
                try {
                    System.out.println("  task2 start");
                    Thread.sleep(1000);
                    System.out.println("  task2 end");
                } catch (InterruptedException e) {
                    System.out.println("task2 interrupted: " + e);
                }
            }
        };
        // 让学生解答某个很难的问题
        pool.execute(task1);

        // 生学生解答很多问题
        for(int i=0; i<1000; ++i){
            pool.execute(task2);
        }

        try {
            // 向学生传达“问题解答完毕后请举手示意！”
            pool.shutdown();

            // 向学生传达“XX分之内解答不完的问题全部带回去作为课后作业！”后老师等待学生答题
            // (所有的任务都结束的时候，返回TRUE)
            if(!pool.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)){
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            System.out.println("awaitTermination interrupted: " + e);
            pool.shutdownNow();
        }

        System.out.println("end");


        /**
         * 可以看出上面程序中waitTime的值比awaitTime大的情况下，发生Timeout然后执行中的线程会中止执行而结束。
         反过来如果缩小waitTime的值，增大awaitTime的值的的话，各个线程就会不被中止的正常运行至结束。
         在这里，如果我们把awaitTime和shutdownNow方法全部屏蔽掉的只留下shutdown方法的话会怎样呢?
         会变成表示main方法结束的「end」显示出来之后，会打印出很多的task2的start和end。
         这就是虽然课程结束了，但是学校仍然不能放学的不正常状态。最恶劣的情况会导致JAVA进程一直残留在OS中。
         所以我们一定不要忘记使用awaitTermination和shutdownNow

         shutdown也是很重要的
         看了上面的描述后可能有些人会认为，只需要执行awaitTermination和shutdownNow就可以正常结束线程池中的线程了。其实不然。
         shutdown方法还有「大家只解答我要求的问题，其它的不用多做」的意思在里面。
         shutdown方法调用后，就不能再继续使用ExecutorService来追加新的任务了，如果继续调用execute方法执行新的任务的话
         就会抛出RejectedExecutionException异常。(submit方法也会抛出上述异常)
         而且，awaitTermination方法也不是在它被调用的时间点上简单得等待任务结束而是在awaitTermination方法调用后，
         持续监视各个任务的状态以或者是否线程已经运行结束。所以不调用shutdown方法执行调用awaitTermination的话由于追加出来的任务可能
         会导致任务状态监视出现偏差而发生预料之外的awaitTermination的Timeout异常

         正确的调用顺序是
         shutdown方法
         awaitTermination方法
         shutdownNow方法(发生异常或者是Timeout的时候)

         实际开发的系统可能会有不能强制线程中止执行的场景出现，所以虽然推荐使用上面说的调用顺序但也并不是绝对一成不变的。
         另外，可以经过一定时间间隔而有计划调用任务执行的ScheduledExecutorService同样适用于上面说的调用顺序，但是在使用scheduled方法的时候需要另外一些步骤。
         */

    }





    /**
     * 今天在看一个线程demo的时候 在线程都submit给线程池了后 线程池调用了shutdown()
     * shutdown()后线程池将变成shutdown状态，此时不接收新任务，但会处理完正在运行的 和 在阻塞队列中等待处理的任务。
     * shutdownNow()后线程池将变成stop状态，此时不接收新任务，不再处理在阻塞队列中等待的任务，还会尝试中断正在处理中的工作线程。
     *
     * 这里再强调下 execute 和 submit()
     * 两个方法都是 execute|submit(Runnable)，都是异步执行一个task，但是submit方法是可以有返回值的，它返回一个Future 对象，
     * 通过这个对象，可以检查这个Runnable实例是否执行完成。
     *
     * Future ：线程的并发模型 具体百度
     * 而且可以 返回异常信息
     *
     * 这里看到shutdown并不阻塞 只是一个通知作用
     *
     *
     */
    public void learnShutDwon(){
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for(int i=0;i<10;i++){
            executor.submit(new MyRunnable(i));
        }
        executor.shutdown();
        System.out.println("shutdown");
    }


    /**
     * 这里 就很明显 如果没有调用shutdown java进程就不会结束
     */
    public void learnShutDwon_1(){
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for(int i=0;i<100000;i++){
            executor.submit(new MyRunnable_1(i));
        }
    }

    public void leaenShutDownNow(){
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for(int i=100;i<110;i++){
            executor.submit(new MyRunnable(i));
        }
        executor.shutdownNow();
        System.out.println("shutdownNow");
    }

    class MyRunnable implements Runnable{

        private int id ;
        MyRunnable(int id){
            this.id = id;
        }


        @Override
        public void run() {
            try {
                System.out.println(this.id+"线程开始休眠");
                Thread.sleep(2000l);
                System.out.println(this.id+"线程开始完成");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MyRunnable_1 implements Runnable{

        private int id ;
        MyRunnable_1(int id){
            this.id = id;
        }


        @Override
        public void run() {
            try {
                System.out.println(this.id+"======thread start!!!!");
                Thread.sleep(1l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
