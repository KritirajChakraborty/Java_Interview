import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class JavaLocks6 {
    public static void main(String[] args) {
        // Other than synchronized and monitor locks, we have other types of locks but why?
        //limitations of synchronized: 1 no control over lock, 2 no timeout, 3 no fairness. Fairness means say 4 threads came to acquire the lock, but the first thread that came got the lock at the last, so it was not fair and it led to thread starvation.
        //There we have a Lock interface, which gives 2 important methods lock() and unlock(). Then we have 4 main types of locks, 1  ReentrantLock, ReadwriteLock, StampedLock and Semaphore.
        
        //Reentrantlock is a reentrant lock, which means if a thread has acquired the lock, it can acquire it again without getting blocked. It also has a fairness policy, which means if we create a ReentrantLock with true, then the threads will acquire the lock in the order they requested it. 
        Resource r1 = new Resource();
        Thread t1 = new Thread(() -> r1.f1(), "T1");
        Thread t2 = new Thread(() -> r1.f1(), "T2");   
        Thread t3 = new Thread(() -> r1.f1(), "T3");
        t1.start();
        t2.start();
        t3.start();
        //Similarity of Reentrantlock and synchronized is same thread can acquire the lock multiple times, say inside a sync method, we call another sync method, the thread can azquire the lock again. But the difference is synchronized is a block, so when the thread exits the block, it releases the lock. But in Reentrantlock, we have to explicitly call unlock() method to release the lock. If we do not call unlock(), then the thread will hold the lock forever and other threads will be blocked forever. So we should always use try finally block to release the lock. Reentrantlock is also same we can call lock.lock() multiple times, but then we have to call unlock() same number of times to release the lock

        //advantage is we have a lock.trylock() method that lets us try to acquire the lock, if we cant we can do other activity in the else block. trylock can be overloaded with a timeout too.see below f2 method.
        Thread t4 = new Thread(() -> {
            try {
                r1.f2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T4");
        t4.start();
        //it also has other methods like isFair(), isLocked(), isLockedByCurrentThread(), getHoldCount() etc. less used
        //ReentrantLock l1 = new ReentrantLock(true); //this is for fairness. Fairness means the order of threads that requested the lock will be same but it is not guaranteed as it is a hint to the scheduler.


        //READWRITELOCK:- Consider what is a resource, it can be anything like file,db,cache where we do both read and write operations. Read ops are non-destructive as they dont change value in the resource but write can be considered destructive, hence we came with the idea of shared locks and exclusive locks. So ReadWriteLock is an interface that has 2 locks,readLock and writeLock. Readlock is a shared lock, which means multiple threads can acquire the readlock at the same time but if a thread has acquired the writelock, then no other thread can acquire either read or write lock. So if we have more reads than writes, then we can use ReadWriteLock to improve performance. But if we have more writes than reads, then we should use ReentrantLock as it will be better performance wise.
        /*
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        Lock readLock = rwLock.readLock();
        Lock writeLock = rwLock.writeLock(); //as the name suggests.
         */
        System.out.println("Starting ReadWriteLock example");
        SharedResource sr = new SharedResource();
        Thread t5 = new Thread(() -> sr.read(), "T5");
        Thread t6 = new Thread(() -> sr.read(), "T6");
        Thread t7 = new Thread(() -> sr.read(), "T7");
        Thread t8 = new Thread(() -> sr.write(10), "T8");
        Thread t9 = new Thread(() -> sr.write(20), "T9");
        Thread t10 = new Thread(() -> sr.write(15), "T10");
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();

        //now we can see that read threads are executing parallelly but write theads will execute one after another. We have another issue with this suppose there are multiple read threads which have occupied the lock on a resource and then a write thread comes but it is not able to occupy the lock since multiple read threads are coming thus it will lead to write starvation of the write thread since  once readlock has acquired the lock, writelock cannot acquire it

        //Another concept here is lock downgrading:- if we have a writelock, we can downgrade to readlock, say we have updated a resource but want to check if we updated correctly we downgrade our writelock to readlock but vice versa is not accepted. Acquire the read lock while still holding the write lock, then release the write lock.
        //Fair mode approximately favors the longest-waiting thread, rather than allowing readers to repeatedly jump ahead of waiting writers.


        //STAMPEDLOCK:- It is a lock with an extra method like tryOptimisticRead() and here the lock.readLock or lock.writeLock returns a long value we say stamp which we use while releasing the lock, in lock or unlock methods its of less use but in tryOptimisticRead we use the long stamp in a method lock.validate(stamp) which checks that inside a method everything untill that point, did the lock change or any other thread was there to change the value of our critical resource, if changed it will give false as a value then we can do a fallover logic. Main thing is tryOptimisticRead does not actually locks the object hence it helps in less overhead which is good for performance, stampedlock is like a modern version of readwritelock. Say in the above example if the validate returns false then we can actually lock the object with lock.readLock then do our fallover logic.      
        System.out.println("Starting STAMPEDLOCK example");
        SharedResourceForStampedLock srp = new SharedResourceForStampedLock();
        Thread t11 = new Thread(() -> srp.readWithOptimistic(), "T11");
        Thread t12 = new Thread(() -> srp.readWithOptimistic(), "T12");
        Thread t13 = new Thread(() -> srp.readWithOptimistic(), "T13");
        Thread t14 = new Thread(() -> srp.write(10), "T14");
        Thread t15 = new Thread(() -> srp.write(20), "T15");
        Thread t16 = new Thread(() -> srp.write(15), "T16");
        t11.start();
        t12.start();
        t13.start();
        t14.start();
        t15.start();
        t16.start();
        //optimistic reads do not block writers. That's why the three writers can update the value while the optimistic readers are sleeping.
        //also stampedlock is not reentrant means we a thread cannot occupy the lock of a object multiple times, otherwise we will be in Deadlock


        //SEMAPHORE:- It is not a lock but rather a synchronization mechanism where we have a permit count which allows a certain number of threads that can access a critical resource. If we instantianize with new Semaphore(1) it means its a binary mechanism where only one thread can enter at a time but if we increase the numbers, it can allows multiple threads also the exit of this threads are non-deterministic as t1 > t2 > t3 can enter but t3 can exit before t1 or t2.
        Semaphore s1 = new Semaphore(4); //means 4 threads can access 
        Semaphore s2 = new Semaphore(3, true); // this means it will respect fairness
        //this is used in API rate limiters and to put limit in parallelly executed task. Diff between between lock and semaphore is there is no ownership by threads in semaphore like they can in locks


        //CONDITION:- 
    
    }
}

class Resource {
    Lock lock = new ReentrantLock();
    void f1() {
        lock.lock();
        try {
            //critical section
            System.out.println("Thread " + Thread.currentThread().getName() + " is executing f1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread " + Thread.currentThread().getName() + " has finished executing f1");
        } finally {
            lock.unlock();
        }
    }

    void f2() throws InterruptedException {
        // if(lock.tryLock()) {
        if(lock.tryLock(2,TimeUnit.SECONDS)) {
            try {
                //critical section
                System.out.println("Thread " + Thread.currentThread().getName() + " is executing f2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread " + Thread.currentThread().getName() + " has finished executing f2");
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("Thread " + Thread.currentThread().getName() + " could not acquire the lock for f2, doing other work");
        }
    }
}

class SharedResource {
    private int val = 0;

    ReadWriteLock rwLock = new ReentrantReadWriteLock();
    Lock readLock = rwLock.readLock();
    Lock writeLock = rwLock.writeLock();
    public int read() {
        readLock.lock();
        try {
            System.out.println("Thread " + Thread.currentThread().getName() + " is reading value: " + val);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return val;
        } finally {
            readLock.unlock();
        }

    }

    public void write(int newValue) {
        writeLock.lock();
        try {
            System.out.println("Thread " + Thread.currentThread().getName() + " is writing value: " + newValue);
            val = newValue;
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            writeLock.unlock();
        }
    }
}

class SharedResourceForStampedLock {
    private int value = 0;

    StampedLock lock = new StampedLock();

    public int read() {
        long stamp = lock.readLock();
        try {
             System.out.println("Thread " + Thread.currentThread().getName() + " is reading value: " + value);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return value;
        } finally {
            lock.unlock(stamp);
        }
    }

    public void write(int newVal) {
        long stamp = lock.writeLock();
        try {
            value = newVal;
             System.out.println("Thread " + Thread.currentThread().getName() + " is writing value: " + value);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock(stamp);
        }
    }

    public int readWithOptimistic() {
        long stamp = lock.tryOptimisticRead();
        int currentValue = value;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if(lock.validate(stamp) == false) {
            //fallover logic 
            //try pessimistic lock
            System.out.println("Fallover logic executed");
            long stampInner = lock.readLock();
            try {
                currentValue = value;;
            } finally {
                lock.unlock(stampInner);
            }
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " is reading value: " + currentValue);
        return currentValue;
    }
}
