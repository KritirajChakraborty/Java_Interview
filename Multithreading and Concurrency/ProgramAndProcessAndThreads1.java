
public class ProgramAndProcessAndThreads1 {

    /**
     * @param args
     */
    public static void main(String[] args) {

        /*
        PROGRAM & PROCESS INTERVIEW NOTES

        Program:
        -> A set of instructions to perform a specific task
        -> Static entity, stored on disk
        -> Not executed until run

        Example:- we have a demo.java file so we we run javac Demo.java, it will create a demo.class bytecode file which we will run. Till  point it is a program

        Process:
        -> An instance of a program in execution
        -> Dynamic entity, resides in memory
        -> Has its own memory space and resources

        Example:- So from above when we run the demo.class file using java Demo, it will create a process in memory which will execute the program. The CPU will allocate memory for the process and it will have its own resources like file handles, network connections, etc.

        CPU INTERACTION: CPU > Registers > Cache > RAM > Disk 
        so program is stored in Disk, when we run it, it will be loaded into RAM and CPU will execute it from there. RAM has fixed memeory so say if we ran vs code, chrome and a game, all 3 will take some space in the ram where they might have variables, CPU time and other resources like file handles, network connections provided by OS. 

        Say we have 2 processes runnig P1 and P2, so all the above will be created for them but they will be isolated from each other. So P1 will not be able to access P2's memory space and resources. This is called process isolation and it is provided by the OS.

        What is a thread?
        -> A thread is a lightweight process that shares the same memory space and resources of its parent process. It is a unit of execution within a process. It can be run independently by CPU. Say we have a java process running so it will always have a main thread and apart from it we can create threads T1 and T2 and lets also consider our CPU is single core(meaning it can run only 1 thread at a time) so it will run main thread for some time then T1 for some time then T2 for some time and then back to main thread and so on. This is called context switching. So all the threads will share the same memory space and resources of the parent process but they will have their own stack memory where they will store their local variables and method calls.

        We know inside JVM memory we have 4 parts, Heap, Stack, Method Area and PC Register(Not same for normal processes run buy the OS). So all the threads will share the same heap memory where they will store their objects and Method area but they will have their own stack memory where they will store their local variables and method calls. So if we have a thread T1 and it creates an object O1 in heap memory and then T2 tries to access O1, it can do that because they share the same heap memory. But if T1 has a local variable V1 in its stack memory, T2 cannot access V1 because it is in T1's stack memory.
        When a java program runs, the JVM creates a main thread and executes the main method. The main thread can create additional threads to perform concurrent tasks. Each thread has its own call stack, but they share the heap memory of the process.

        Concurrency and Parallelism: Concurrency is the ability of a system to run multiple tasks in overlapping time periods, while parallelism is the ability to run multiple tasks simultaneously. In a single-core CPU, concurrency is achieved through context switching, while in a multi-core CPU, parallelism can be achieved by running threads on different cores. Also if we hve more thread than cores than the CPU we run them parallely but each core will run the threads concurrently using context switching.
        */

        /*
        There are 2 ways to create threads in java, by extending Thread class and by implementing Runnable interface. The main difference between the two is that when we extend Thread class, we cannot extend any other class because java does not support multiple inheritance but when we implement Runnable interface, we can extend another class as well. Also when we extend Thread class, we are creating a new thread object and when we implement Runnable interface, we are creating a new thread object and passing the Runnable object to it. SEE BELOW
        */
       //extending thread
       Thread t1 = new MyThread(); //creating a thread object by extending Thread class
       t1.start(); //starting the thread
       //implementing Runnable
       MyRunnable myRunnable = new MyRunnable(); //creating a Runnable object
       Thread t2 = new Thread(myRunnable); //creating a thread object and passing the Runnable object to it
       t2.start(); //starting the thread
       //Benefits of implementing Runnable interface over extending Thread class:
       //1 We can extend another class as well
       //2 We can share the same Runnable object among multiple threads
       //3 We can separate the task from the thread, making it more flexible and reusable   
       //4 We can use lambda expressions to create threads in a more concise way
       //5 We can use thread pools to manage threads more efficiently how?*

       //for point 4 we can do it like this also because it is a functional interface 
       //type 3 
       Thread t3 = new Thread(() -> {
           System.out.println("Thread is running" + " " + System.currentTimeMillis());
       });
       t3.start();

       //Threads also have a name and id(now depracated) which helps in debugging multithreaded app in production as now we have the name of the thread we want to trace
       //To get the name of the main thread --
       System.out.println("Main thread name: " + Thread.currentThread().getName()); // name is main
       System.out.println("Main thread ID: " + Thread.currentThread().getId());
        Thread t4 = new Thread(() -> {
            System.out.println("Thread is running" + " " + System.currentTimeMillis());
            System.out.println("Thread name: " + Thread.currentThread().getName());
            System.out.println("Thread ID: " + Thread.currentThread().getId());
        });
        t4.start();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(); 
        //we can never do t4.run() because it will not create a new thread and will run in the main thread only. So we can never call run() method directly, we have to call start() method which will tell the OS to create a new thread and then call run() method internally.
        //Also there is no order when which threads will run so we cannot predict the order of execution of threads. It is not deterministic.

        //Another imp interview Q is Can we start a thread twice? The answer is No, we cannot start a thread twice. Once a thread is started, it cannot be started again. If we try to start a thread again, it will throw an IllegalThreadStateException. So we can only start a thread once and then it will go to terminated state after execution. If we want to run the same task again, we have to create a new thread object and start it again.
        //CPU has something called register which stores the intermidiete results. Also it is very small in size
        //Why threads are non deterministic? Because we cannot predict the order of execution of threads. It depends on the OS scheduler and the CPU time slice allocated to each thread. Also it depends on the number of cores in the CPU and the number of threads running in the process. If we have more threads than cores, then the threads will run concurrently using context switching. If we have less threads than cores, then the threads will run in parallel. So we cannot predict the order of execution of threads.


        //THREAD LIFE CYCLE: New -> Runnable -> Running -> Waiting/Blocked -> Terminated
        //New: When a thread is created using new Thread() or new MyThread(), it is in the new state. It is not yet started and has not been scheduled for execution.
        
        //Runnable: When a thread is started using start() method, it is in the runnable state. It is ready to run and waiting for the CPU to allocate time slice to it. It can be in runnable state for a long time if there are many threads in the process and the CPU is busy with other threads.
        
        //Running: When the CPU allocates time slice to a thread, it is in the running state. It is executing its run() method and performing its task. It can be in running state for a short time if the CPU time slice is small or if the thread is preempted by another thread with higher priority.
        
        //Waiting/Blocked: When a thread is waiting for some resource or event, it is in the waiting/blocked state. It is not executing its run() method and is waiting for the resource or event to be available. It can be in waiting/blocked state for a long time if the resource or event is not available.
        
        //Terminated: When a thread completes its task or is stopped by another thread, it is in the terminated state. It is not executing its run() method and cannot be started again. It can be in terminated state for a long time if the thread is not garbage collected by the JVM.
        
        //*Lock and synchronization: When multiple threads are accessing shared resources, we need to synchronize the access to avoid data inconsistency and race conditions. We can use synchronized blocks or methods to lock the shared resources and allow only one thread to access them at a time. We can also use ReentrantLock or other locking mechanisms provided by java.util.concurrent package for more advanced locking features. So when one thread is working on a critical resource it will block other threads from accessing it until it is done and the releases the lock. This is called mutual exclusion and it is a fundamental concept in concurrent programming.
        //Also in Java we consider Runnable and Running a single state.
        //We also have a timed waiting state where a thread can wait for a specific time. we use sleep(), join() and wait() methods to put a thread in timed waiting state. So when a thread is in timed waiting state, it will wait for the specified time and then it will go back to runnable state. If the thread is interrupted while it is in timed waiting state, it will throw InterruptedException and go back to runnable state. So we can use try-catch block to handle InterruptedException and take appropriate action.
        //we also have a stop() where we can stop a thread but it is deprecated because it is unsafe and can cause data inconsistency and race conditions as it will stop the thread from any other state 

        Thread mainThread = Thread.currentThread();
        Thread t5 = new Thread(() -> {
            System.out.println("Thread is running" + " " + Thread.currentThread().getName());
            System.out.println("State of main thread " + mainThread.getState()); //here main will be in TIMED WAITING state because it is sleeping for 2 seconds and t5 is running in parallel
        });

        System.out.println("State of thread t5 before start(): " + t5.getState()); // NEW
        t5.start();
        System.out.println("State of thread t5 after start(): " + t5.getState()); // RUNNABLE or Terminated(rarely)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("State of thread t5: " + t5.getState()); // TERMINATED
    }
}    
//type 1
class MyThread extends Thread {
    public void run() {
        //logic inside threads is written inside run()  
        System.out.println("Thread is running" + " " + System.currentTimeMillis());
    }
}
//what happens is when we call t1.start(), JVM asks the OS to create a new thread and it gets its stack space/PC and then internally run() is called and the thread is executed

//type 2 Runnable
class MyRunnable implements Runnable {
   @Override    
    public void run() {
        System.out.println("Runnable thread is running" + " " + System.currentTimeMillis());
    }
}
//internally for extending Thread, runnable is created first as Thread implements Runnable interface.
