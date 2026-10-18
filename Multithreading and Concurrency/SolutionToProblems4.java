public class SolutionToProblems4 {
    public static void main(String[] args) {
        //synchronized keyword: The synchronized keyword is used to ensure that only one thread can access a method or block of code at a time. It is used to avoid race conditions and ensure thread safety. When a thread enters a synchronized method or block, it acquires a lock on the object, and other threads that try to access the same method or block will be blocked(they will be in blocked state) until the lock is released. This ensures that only one thread can execute the synchronized code at a time, preventing race conditions and ensuring that shared resources are accessed in a thread-safe manner
        /*
        1. t1 enters the synchronized method increment() and acquires the lock on the Counter object.
        2. t2 tries to enter the synchronized method increment() but is blocked because t1 holds the lock on the Counter object.
        3. t1 completes its execution of the increment() method and releases the lock on the Counter object.
        4. t2 acquires the lock on the Counter object and enters the synchronized method increment().
        5. t2 completes its execution of the increment() method and releases the lock on the Counter object.
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
        System.out.println("Final count: " + c1.count); //Expected 20000, and it will be 20000 because we have used synchronized keyword to avoid race condition.
        /*  synchronized is good for: 
            1. to protect shared data
            2. to make any operation atomic
            3. to ensure visibility
            4. to prevent reordering of code by compiler or CPU
        */ 
       
        Test test = new Test();
        Thread t3 = new Thread(() -> {
            test.show();
        }); 
        Thread t4 = new Thread(() -> {
            test.show();
        }); 
        t3.start();
        t4.start();
        try {
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       //It is also called/used Monitor Locks or Object Locks because when we write sync on a method of a object, it acquires the lock on the object and not on the method. every object in java has an internal lock. Also say we have a method that does a lot o normal work and only little with shared data, in that case it is not good to put synchronised on the method as it will overhead the performance, in that case we can use synchronised block. see object below

       Test1 test1 = new Test1();
        Thread t5 = new Thread(() -> {
            test1.m1();
        }); 
        Thread t6 = new Thread(() -> {
           test1.m2();
        }); 
        t5.start();
        t6.start();
        try {
            Thread.sleep(2000);
            t5.join();
            t6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    } 
}
class Counter {
    public int count = 0;
    synchronized void increment() {
        count++;
    }
    /*
    instead of synchronizing the entire method we can
    void increment() {
       //normal code
       synchronized(this) {
            count++;
       }
       //normal code
    }
    */
}

//checking how synchronized works
class Test {
    //first check without synchronized, then with synchronized
    synchronized void show(){
        System.out.println(Thread.currentThread().getName() + " starts");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " ends");
    }
}

//understanding object locks 
class Test1 {
    //first check with sync on both, then sync on both and only on one.
    //without syn on both, both will start paralelly and end parallely, with sync on both, one will start and end then other will start and end.
     synchronized void m1() {
        System.out.println("m1 starts");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m1 ends");
    }
    synchronized void m2() {
        System.out.println("m2 starts");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m2 ends");
    }
}