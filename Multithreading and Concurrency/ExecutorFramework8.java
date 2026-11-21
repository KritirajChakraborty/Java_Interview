import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorFramework8 {

    public static void main(String[] args) {
        //Problem with manual creation of threads
        //Memeory allocation: One thread in java cost 512kb to 1mb, say we have 1000 cocurrent threads running, it would cost the ram 512mb - 1gb spacec
        //OS Scheduling: If have more number of threads say 300x per code, then the CPU's will spend more time in context-switching rather than actual work. This process is called Thrashing
        //Thread creation time: it takes time since it has its own stack space, Program Counter etc, manually we are creating and terminating threads

        //Hence came Executor Framework
        //under which comes Threadpool: its like a ready made pool of a number of threads we call workers. We give the executor framework certain tasks and it assigns it to the workers and after completing the task the worker instead of terminating, it becomes available again under the threadpool
        //we also have a queue that stored task that need to be handled when workers are available again
        //if worker is not available and queue is full, we discard the task sometimes unless we threadpool created new workers
        //now what are these tasks? Runnable

        //example
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for(int i = 0; i < 5; i++) {
            int taskId = i;
            executor.execute(() -> System.out.println("Task " + taskId + " performed by " + Thread.currentThread().getName()));
        }
        //always shut down exector after finishing task
        //executor.shutdown();

        //Heirarchy of Executor Framework
        /*
                        Executor (Functional Interface, only has execute method)
                                            |
                                  ExecutorService (Interface, many mehthods)
                            /              |                   \ 
        ThreadPoolExecutor(Class)  ForkJoinPool(Class)  ScheduledExecutorService
                                                            (Interface) |
                                            ScheduledThreadPoolExecutor(Class)
        */

        //methods are ExecutorService: execute() takes as runnable and executes it, other than this we have a submit() which takes a callable and executes it. The diff between Callable and Runnable is Callable returns something. To catch what callable returns we use Future<Type> of List<Future>. Callable internally calls calls() instead of runs()
        Future<Integer> f1 = executor.submit(() -> 10);
        try {
            Integer val = f1.get(); //calling this blocks the thread that executes it until the value is retrieved/returned
        } catch (Exception e) {
            // TODO: handle exception
        } 
        //why always use shutdown() because its does:- let the ongoing tasks to be executed while not accepting new tasks and when all tasks are executed, terminate all threads
        //we have a shutdownNow() this TRIES to shut down ongoing tasks but final call is with OS
        //we have invokeAll() and invokeAny() methods: invokeAll runs list of callable tasks and invokeAny returns the first exeuted task

        //Runnable exeptions are lost but we can catch exeptions in Callable. 
        // try {
        //     executor.execute(() -> {
        //     int x = 10 / 0;
        // });
        // } catch (Exception e) {
        //     System.out.println("Exception catched from Runnable");
        //     //This wont run because the exeption happened in another thread and we are trying to catch it in main thread
        // }
        //but
        Future<Integer> f2 = executor.submit(() -> {
            return 10 / 0;
        });
        try {
            Integer ans = f2.get();
        } catch (Exception e) {
            System.out.println("Exeption Catched from Callable + " + e.getStackTrace()); //it is printed unlike the Runnable catch
        }
        //*to catch exeptions from .execute we have a ThreadFactory

        //Threadpool is a pool of threads/workers + waiting queue for upcoming tasks
        //we can create our own threadpool but it would be cumbersome thats why we have a Utility class Executors that gives us a already made ExecutorThreadPool of many types, the above newFixedThreadPool is one of them
        // new ThreadPoolExecutor() this takes int currentPoolSize, int maxPoolSize, long keepTimeALive, TimeUnit unit, BlockingQueue<Runnable> queue
        // currentPoolSize is the number of threads that will always be alive in our pool
        //maxPoolSize is the max number of threads that can be created when tasks > currentPoolSiza
        //keepTimeALive and TimeUnit(SECONDS): it means if after keepTimeAlive seconds idle workers/threads will be terminated
        //BQ<Runnable> queue its when we have more task than threads, tasks go in this waiting queue until Executor creates new threads if curr < max otherwise discard(depends on type of thread pool)
        /*
        Rules/Configuration for ExecutorThreadPool

            if(curr < max) {
            create more thread
            } else if (queue.inNotFull) {
             push into queue
            } else if (threads < max) {
              create new thread and take tasks from queue 
            } else (final) {
             reject the task //rarely happens
            }
        */ 
       //if we try to make own ThreadPoolExecutor
       ThreadPoolExecutor executor1 = new ThreadPoolExecutor
                                            (2,5,10,TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));

        //it will work the same like Executors.newFixedPoolExecutor
        for(int i = 0; i < 5; i++) { //check with 100 to see rejected exeption
            int taskId = i;
            executor1.execute(() -> System.out.println("Task " + taskId + " performed by " + Thread.currentThread().getName()));
        }
        /*
            java.util.concurrent.RejectedExecutionException: Task ExecutorFramework8$$Lambda/0x0000000022043000@330bedb4 rejected from java.util.concurrent.ThreadPoolExecutor@4554617c[Running, pool size = 5, active threads = 5, queued tasks = 5, completed tasks = 0]
            this error happens when the last else runs of the rules of ThreadPool 
        */
       System.out.println("GAAAAAAAAAAAAAAAAAAAAAAAAAAAAP");
        //we write new ThreadPoolExecutor when we want to control the configuration like poolsize, max poolsize, queue type


        //type of queues: ArrayBlockingQueue has a fixed size which is used by newFixedThreadPool
        //LinkedBlockingQueue: can be scaled infinitely as there is not size limit

        //type of threadpools
        //1 FixedThreadPool we used till now, it is configured with a fixed number of theads and unbounded queue
        //2 CachedThreadPool, it has unlimited number of threads and no queue(null for 5th parameter). it creates new thread for every new task
        //3 SingleThreadPool, it has a single thread and a queue(it does 1 task after another) it makes the task serielizable. works in login request so we dont miss any request
        //4 ScheduledThreadPool: it runs task in future, it has a method .schedule(runnable, time, TimeUnit). another method scheduleAtFixedRate(runnable, initial time, fixed rate, TimeUnit) it will happen first at initial time and then keeps executing at fixed rate

        //what happens when queue is full and we have reached maxPoolSize of Threads, so in this case with have RejectionPolicy: in which we have
        //1 AbortPolicy:- it throws a RejectException like above
        //2 DiscardPolicy:- it is silently discarded, Not used much because of data loss
        //3 DiscardOldestPolicy:- discard the oldest policy and try to work on the next one

        ExecutorService executor2 = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++) {
            int taskId = i;
            executor2.execute(() -> System.out.println("Task " + taskId + " performed by " + Thread.currentThread().getName()));

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        executor.shutdown();
        executor1.shutdown();
        executor2.shutdown();
        
    }
    
}
