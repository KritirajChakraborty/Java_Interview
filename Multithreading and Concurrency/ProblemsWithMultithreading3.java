public class ProblemsWithMultithreading3 {
    //to see visibility issues.
    static volatile boolean flag = false;
    public static void main(String[] args) {
        //EXAMPLE race conditions, Visibility issues, and deadlocks are common problems that can occur in multithreaded programs. These issues can lead to unpredictable behavior and can be difficult to debug. It is important to use proper synchronization techniques and thread-safe data structures to avoid these problems.

        //Race conditions:
        //this is caused when some operations which are not atomic in nature and multiple thread are trying to access the same resource.
        //check-then-act is a common example of race condition. it happens if we check some condtion before executing our code.
        //Similarly with double values: since it is a 64 bit value, and JVM stores it in two 32 bit chunks, so if one thread is trying to read the value while another thread is trying to write the value, then the reading thread may get a corrupted value.

        /*
        consider this int a = 10, then a++; is not atomic operation, it is actually a combination of three operations:
        1. read the value of a
        2. increment the value of a
        3. write the value of a back to memory
        */

        Counter c1 = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                c1.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                c1.increment();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final count: " + c1.count); //Expected 20000, but may not be due to race condition. Because threads are trying to access the same resource at the same time, and the increment operation is not atomic, so the final value of count may be less than 20000. Happens during parallel execution and concurrent execution(centext switching makes it non deterministic) also.

        //Critical section: A critical section is a part of the code that accesses a shared resource and must not be executed by more than one thread at a time. In the above example, the increment() method is a critical section because it accesses the shared resource count. To avoid race conditions, we can use synchronization techniques like synchronized blocks or locks to ensure that only one thread can execute the critical section at a time. And count is the shared resource.

        //Atomicity Problem: In java we say we have 2 types of operations, atomic and non-atomic. Like reading only a value is atomic, writing alone is also atomic, but together they are not. to solve this we use synchronised keyword or Atomic Integers. btw all assignment works are atomic.

        /*check-then-act problem: This is a common example of race condition. It happens if we check some condition before executing our code. say we have a banking app, which has a balance, now 2 threads are coming to deduct 500 from the balance but we have 1000 rs initially, pseudocode:
        if(bal>= 500) {
        bal = bal - 500;}
        So here say when t1 was validating the if check, at that time context switching happens and t2 comes and it checks the bal was more then 500 and deducts, then t1 comes back and since it has done the if check, it will directly go and deduct 500, effective now you have -100 which is a failure of our system.
        */



       //Visibility issues: Visibility issues occur when one thread modifies a shared variable, but the change is not visible to other threads. This can happen due to caching or compiler optimizations. To avoid visibility issues, we can use the volatile keyword or synchronization techniques to ensure that changes made by one thread are visible to other threads. Example we have 2 CPU cores which have their own cache, and there are 2 threads running on it. Both of them first read a variable that was false in the starting, then CPUs cache it, then one thread changes it to true which lead to change in the common memory, but since other thread only reads the value, it read from the cache every time, so it will never see the change made by the other thread. Here we can use volatile keyword to avoid this issue.
       Thread t3 = new Thread(() -> {
            while (!flag) {
                //do nothing
            }
            System.out.println("Thread 3: Flag is true"); //this never prints because of visibility issue, since t3 is reading the value from its cache and not from the main memory where t4 has rewritten the value. 
            // TO SOLVE THIS ISSUE, we use the volatile keyword, which tells the JVM that the variable can be modified by multiple threads and should not be cached. This ensures that the value of the variable is always read from the main memory, and any changes made by one thread are visible to other threads.
        });
        Thread t4 = new Thread(() -> {
            try {
                Thread.sleep(1000); // Sleep for 1 second to simulate some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
            System.out.println("Thread 4: Flag is set to true");
        });
        t3.start();
        t4.start();
        //VOLATILE cannot solve atomicity problem because it only ensures visibility, but does not provide mutual exclusion. So if we have a volatile variable that is being incremented by multiple threads, we can still have race conditions. To solve atomicity problems, we need to use synchronization techniques like synchronized blocks or locks to ensure that only one thread can execute the critical section at a time. And count is the shared resource.



        //Ordering issues: Ordering issues occur when threads read/write from a shared memory, if we have 2 variables line by line, it doesnot mean compiler will execcute them in the same order, it can reorder them for optimization. So if we have 2 threads, and one thread is writing to a variable and another thread is reading from it, the reading thread may see the old value of the variable instead of the new value. 
        /*
        int a = 0;
        boolean flag = false;
        say when one thread updates the value of a to 1 and sets the flag to true, but due to reordering, the reading thread may see the flag as true before it sees the updated value of a and if 2nd thread checks if flag is true it prints the value of a, it will print 0 instead of 1. This leads to data consistency issue in out application. We can use volatile keyword as well as synchronised to avoid this issue. Volatile ensures that the value of the variable is always read from the main memory, and any changes made by one thread are visible to other threads. Synchronization ensures that only one thread can execute the critical section at a time, which prevents race conditions and ensures that the order of execution is maintained.

        volatile ensures read/write and ordering but not atomicity, synchronised ensures all but it is heavy on performance because of locking and unlocking.
        */
    }
}

//class to demonstrate race condition
class Counter {
    public int count = 0;
    void increment() {
        count++;
    }
}
