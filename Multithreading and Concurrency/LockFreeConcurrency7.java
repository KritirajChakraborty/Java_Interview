import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeConcurrency7 {
    public static void main(String[] args) {
        //We have a problem of overhead and thus slow execution with the use of synchronised or locks. In order to overcome this we came through Atomic Variables. See class 1 and 2 below
        //class Counter with a normal int or Integer will face race conditions, so to fix this we could use synchronised on the method or locks but that add a overhead we dont need. Here we can use AtomicIntegers whose methods are atomic so when set,get,incrementAndSet,setAndIncrement,decrementAndSet,addAndGet etc are called, there is context switching in between but the methods are implemented using CAS(Compare-And-Set). So we will always get the correct value
        //like integers we have AtomicLong and AtomicBoolean which we can use in similar way

        Counter c1 = new Counter();
        Thread t1 = new Thread(() -> {
            for(int i = 1; i <= 10000; i++) {
                c1.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for(int i = 1; i <= 10000; i++) {
                c1.increment();
            }
        });

        t1.start();
        t2.start();

        //sleep the main thread for sometime so above 2 thread can complete task then we can print the val
        try {
           Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println(c1.val);


        //We also have AtomicReference for non-primitive values. Suppose you are booking for a movie ticket for a particular seat but there are other people who are trying to book, see 2nd class SeatBooking below
        //Problem with if 2 thread are calling the book method which is doing a check-then-act operation and during the check if context-switching happens or both execute together both people with get success = true but name variable will hold only one value.
        //we also have AtomicStampedReference that checks the reference address along with a versioned stamp to see if both ref and version has changed. Used to fix ABA problem, say T1 reads a object ref A and pauses, T2 reads A and changed to B, then again changes B to A, pauses, T1 reads A again and attempts CAS and succeeds since ref has no version and is same.

        SeatBooking s1 = new SeatBooking();
        Thread t3 = new Thread(()-> {
            boolean val = s1.book("Kritiraj");
            System.out.println("T1 says " + val);
        });
        Thread t4 = new Thread(()-> {
            boolean val = s1.book("Alexander");
            System.out.println("T2 says " + val);
        });
        t3.start();
        t4.start();
        
        //sleep main thread
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println(s1.name);
        /*
        T1 says true
        T2 says true
        Kritiraj    this is the output with normal string, now we will use AtomicReference and its method of compareAndSet() which is atomic. see same SeatBooking class edited
        */
        // compareAndSet(): What happens during concurrency and parallelism?
        // Multiple threads can execute compareAndSet() simultaneously.
        // However, compareAndSet() is an atomic operation.
        //
        // It checks whether the current value matches the expected value
        // and updates it only if the comparison succeeds.
        //
        // When multiple threads try to update the same variable from the
        // same expected value, only one thread can successfully perform
        // that particular update. The remaining threads get false because
        // the current value no longer matches their expected value.
        //
        // Internally, the JVM uses hardware-supported atomic operations
        // and CPU cache-coherence mechanisms to ensure this operation
        // behaves as one indivisible operation.
        //
        // This prevents race conditions during the compare-and-update
        // operation, without requiring us to explicitly use synchronized
        // or ReentrantLock.

        //See CAS retry in 3rd class below. Compare and set internally does compare and swap and retry logic
        LikeCounter l1 = new LikeCounter();
        Thread t5 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        Thread t6 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        Thread t7 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        Thread t8 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        Thread t9 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        Thread t10 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        Thread t11 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        Thread t12 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        Thread t13 = new Thread(() -> {
            for(int i = 1; i <= 100; i++) {
                l1.like();
            }
        });
        
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
        t10.start();
        t11.start();
        t12.start();
        t13.start();

        try {
            Thread.sleep(3);
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println(l1.getLikes());

        //Also instead of using the AtomicRef of Integer we could have used the AtomicInteger directly and no need to implement our retry logic since AtomicInteger class does that on its own along with AtomicLong and AtomicBoolean
        //Note: anyAtomicVariable.set(anyAtomicVariable.get()+1) this is not a atomic operations set and get are individually atomic but together they are not and context switching can happen between get and set. Same applies fot if checks

    }
}

class Counter {
    //int val = 0; //with a normal int or Integer we will face race condition
    AtomicInteger val = new AtomicInteger(0);
    void increment() {
        //this.val++;
        this.val.incrementAndGet(); //this method is atomic 
    }
}

class SeatBooking {
    /* Not atomic 
    String name = "EMPTY"; 
    boolean book(String newName) {
        if(this.name.equals("EMPTY")) {
            this.name = newName;
            return true;
        }
        return false;
    }
    */
   AtomicReference<String> name = new AtomicReference<>("EMPTY");
   boolean book(String newName) {

        String current = this.name.get();
        if(current.equals("EMPTY") == false) {
            //means checking negative output first for early return
            return false;
        }
        return name.compareAndSet("EMPTY", newName); //returns a boolean if ops is successful 1st arg for checking and 2nd arg new value
   }
}

class LikeCounter {
    AtomicReference<Integer> totalCount = new AtomicReference<>(0);
    void like() {
        Integer currentCount;
        Integer finalCount;
        //below is the rough implementation of compare-and-swap with retry logic
        while(true) {

            //1. get the latest value of the count/variable
            currentCount = totalCount.get();
            //2. Increment/modify the variable with the incoming input if applicable
            finalCount = currentCount + 1;
            //3. compare if latest value if same and not changed by other threads in between
            if(totalCount.compareAndSet(currentCount, finalCount)) {
                return;
            }
            //if thread has reached here means the value was changed in between ops, HENCE re-try(done by the infinite while loop)
            System.out.println("Confict detected. Re-trying...");
        }
    }
    int getLikes() {
        return totalCount.get();
    }
}
//above class if written with AtomicInteger
class LikeCounter1 {
    AtomicInteger totalCount = new AtomicInteger(0);
    void like() {
      totalCount.incrementAndGet();
    }
    int getLikes() {
        return totalCount.get();
    }
}

