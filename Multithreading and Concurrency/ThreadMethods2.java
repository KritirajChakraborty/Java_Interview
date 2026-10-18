public class ThreadMethods2 {
    public static void main(String[] args) {
       //sleep() it sleeps the current method for a specific time where it will go to timed waiting state.
       //important: when a thread is put to sleep it doesnt release the Lock it holds, so other threads waiting for the lock will not be able to access the resource until the thread wakes up and releases the lock.
       try {
            Thread.sleep(1000); //always use this method inside try catch because it thows checked exception
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //join() it is used to pause the execution of the current thread until the thread on which join() is called is dead or terminated. It is used to make sure that a thread has completed its execution before another thread starts executing.
        /*
        System.out.println("Main Thread starts");
        Thread t1 = new Thread(() -> { 
            System.out.println("Thread-0 starts");
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread 1: " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Thread-0 ends");
        });
        t1.start();
        try {
            t1.join(); // Main thread will wait until t1 completes
        } catch (InterruptedException e) {
            e.printStackTrace();
        } //if we comment this block and run again you will see the below sop line executed even before the thread t1 starts. So join on any thread tells the current thread to wait untill that thread completes its execution then current thread will continue.
        //join also has some overloaded methods which can be used to specify the time for which the current thread should wait for the other thread to complete its execution. If the other thread does not complete its execution within the specified time, the current thread will continue its execution.
        System.out.println("Main Thread ends");
         */

        //yield() it is used to pause the execution of the current thread and allow other threads of the same priority to execute. It is used to improve the performance of the application by allowing other threads to execute when the current thread is not doing any useful work. It is a static method of the Thread class and can be called using Thread.yield(). It is important to note that yield() does not guarantee that the current thread will be paused, it only gives a hint to the thread scheduler that the current thread is willing to yield its current use of a processor. The thread scheduler may or may not choose to pause the current thread based on its own scheduling algorithm. So at the end final decision is with OS. Also current thread doesnt go into RUNNABLE, WAITING or TIMIED_WAITING, BLOCKED state, it just goes back to the ready queue and waits for its turn to execute again. So it is not a good idea to use yield() method in production code as it may lead to unpredictable behavior of the application.


        //interrupt() it is used to interrupt a thread that is currently in a waiting or sleeping state. It is used to stop a thread from executing when it is no longer needed or when it is taking too long to complete its task. When a thread is interrupted, it will throw an InterruptedException and the thread will be terminated. It is important to note that interrupt() does not stop a thread immediately, it only sets the interrupted status of the thread to true. The thread will continue to execute until it reaches a point where it checks for the interrupted status and throws an InterruptedException. So it is important to handle the InterruptedException properly in the code to ensure that the thread is terminated gracefully. Also used to stop the thread pool
        //diff between interrupted() and isInterrupted() is that interrupted() is a static method that checks the interrupted status of the current thread and clears the interrupted status, while isInterrupted() is an instance method that checks the interrupted status of the thread on which it is called without clearing the interrupted status. So if you want to check the interrupted status of a thread without clearing it, you should use isInterrupted(), but if you want to check the interrupted status of the current thread and clear it, you should use interrupted().
        /*
        System.out.println("Main Thread starts");
        Thread t2 = new Thread(() -> {
            System.out.println("Thread-1 starts");
           while(!Thread.currentThread().isInterrupted()) {
                    System.out.println("Thread-1 is interrupted");
            }
            System.out.println("Thread-1 ends");
        });
        t2.start();
        try {
            Thread.sleep(1000); // Let the thread run for a bit
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.interrupt(); // Interrupt the thread
        System.out.println("Main Thread ends");
         */

        

        //isAlive() it is used to check if a thread is currently running or not. It returns true if the thread is alive and false if the thread has completed its execution or has been terminated. It is important to note that a thread can be alive even if it is in a waiting or sleeping state, as long as it has not completed its execution or has not been terminated. So it is a good idea to use isAlive() method to check the status of a thread before performing any operations on it.
         

        //currentThread() it is used to get the reference of the currently executing thread. It returns a reference to the current thread object, which can be used to get information about the thread, such as its name, priority, and state. It is important to note that currentThread() is a static method of the Thread class and can be called using Thread.currentThread(). It is a good idea to use currentThread() method to get the reference of the current thread before performing any operations on it. we have other methods with currentThread() like getName(), getId(), getState(), getPriority() etc. which can be used to get the name, id, state and priority of the current thread respectively. And also set them too


        //getPriority() it is used to get the priority of a thread. It returns an integer value between 1 and 10, where 1 is the lowest priority and 10 is the highest priority. The default priority of a thread is 5. It is important to note that the priority of a thread does not guarantee that it will be executed before other threads with lower priority, as the thread scheduler(OS) may choose to execute threads based on other factors such as their state and the availability of resources. So it is a good idea to use getPriority() method to get the priority of a thread before performing any operations on it.
        //MIN_PRIORITY = 1
        //NORM_PRIORITY = 5 //DEFAULT for all threads
        //MAX_PRIORITY = 10


        //Daemon Threads: Background running threads are called Daemon threads. They are used to perform background tasks such as garbage collection, logging, and monitoring. They are low-priority threads that run in the background and do not prevent the JVM from exiting when all user threads have completed their execution. Daemon threads are created by calling the setDaemon(true) method on a thread before it is started. It is important to note that daemon threads should not be used for tasks that require user interaction or that need to be completed before the application exits, as they may be terminated abruptly when the JVM exits. So it is a good idea to use daemon threads for background tasks that do not require user interaction or that can be safely terminated when the application exits.
        /*
        Thread t3 = new Thread(() -> {
           while(true){ 
            System.out.println("Daemon Thread is running");
            }
        });
        t3.setDaemon(true); // Set the thread as a daemon thread
        t3.start(); 
        //normally t3 will run as immedietely after stating the thread we returned from the main thread doest matter but if we set daemon to true then t3 will not even be able to run sleep the main thread for 2 sec to see
        try {
            Thread.sleep(2000); // Let the main thread sleep for a bit
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
         */

    }
    
}
