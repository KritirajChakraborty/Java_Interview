public class InterThreadCommunication5 {
    
    public static void main(String[] args) {
        //problem with inter com between thread. classic example the producer-consumer problem. below Box object is used to illustrate this. The problem here arises when 2 threads are working on a single box object. What if before the producer thread could produce value, the consummer thread consumes the value. 2nd problem is producer has produced the value but before consumer could consume it, producer again produced another value but the previous value was lost and not consumed. We might think in order to fix this we can use synchronized keyword on the method which will acquire lock of the box object but this does not actually fix the situation since they keyword only guarantees only 1 thread can access the method at a time.
        Box box = new Box();
        /*
        Thread t1 = new Thread(() -> box.producer(1));
        Thread t2 = new Thread(() -> box.consumer());
        t1.start();
        t2.start();
         */
         //if we run this many times we will see sometimes consumer consumed null will run first. For better understanding we will add a loop inside the thread.
         
        Thread t3 = new Thread(() -> {
            for(int i=0; i<10; i++) {
                box.producer(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t4 = new Thread(() -> {
            for(int i=0; i<10; i++) {
                box.consumer();
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t3.start();
        t4.start();
        //above we found the problem where many times consumer is consuming null value. Our next thought would be to use infinite loops. see below Box added while loop to both methods. But now after adding the infinite loop, we face another problem of dead lock, say producer has produced the value and turned the the flag to true but before printing consumer thread runs due to context switching, then consumer prints consumed first before producer prints produced for the same value, that is an example of race condition. Another is say producer has produced 1 then consumer has consumed 1 then when producer was suppose to produce 2, the switch happened and consumer checks item is null and goes into infinite loop. Also we did not use volatile keyword here thats why values were getting cached and read from cache. After adding the volatile word the infinite loop will go but we will see consumer consuming a value before producer basically our first problem of race condition from above. So we might think we will add synchronized to the methods now. But if we do, say first consumer runs and acquires the lock, see the value as null and goes into infinite loop, then context switching happens but producer cannot enter method since consumer has the lock thus we are in a deadlock now.

        // So for this kinda problem we have wait() and notify() methods. What wait does is it releases the lock and puts the thread in a WAITING state until another thread wakes it up using notify. After notify, the waiting thread will go to BLOCKED state where it will wait until it can acquire the lock again. Say consumer enters first and goes into infinite loop, there we call the wait() method so consumer thread goes into waiting state after releasing the lock, producer enters acquires the lock, produces the value and calls notify method which wakes up the consumer thread, consumer thread goes into BLOCKED state and waits to acquire the lock, once producer releases the lock, consumer acquires it and consumes the value. This is how we can solve the problem of inter thread communication. Also wait will not work without synchronized keyword since wait and notify methods are part of the object lock. So we need to use synchronized keyword along with wait and notify methods. If we try to call wait on a non synchronized method, we will get IllegalMonitorStateException. Also when the blocked thread acquires the lock again, it will not start from the beginning of the method but from the point where it called the wait method. 
        //A. wait(), notify() and notifyAll() belong to Object, not Thread. sleep() belongs to Thread. Also sleep does not release any locks

        //Now let's understand why using `notify()` instead of `notifyAll()` can cause a deadlock-like situation when we have multiple producers and consumers. Say we have 2 producers P1, P2 and 2 consumers C1, C2 working on the same Box object. Initially, the box is empty, so say C1 enters first, checks the condition `item == null` and calls `wait()`, releasing the lock and going into WAITING state. Now C2 enters, finds the box empty and also calls `wait()`. Next P1 enters, acquires the lock, produces item 1 and calls `notify()`, which randomly wakes up C1. However, before C1 can reacquire the lock, P2 acquires it. Since the box already contains item 1, P2 finds `item != null` and calls `wait()`, releasing the lock. Now C1 acquires the lock, consumes item 1, sets item to null and calls `notify()`. This time, suppose C2 gets notified instead of P2. C2 wakes up, reacquires the lock, checks `item == null` and finds it true because the box is empty, so it calls `wait()` again. Now P1 acquires the lock, produces item 2 and calls `notify()`, which wakes up P2. Before P2 can reacquire the lock, P1 releases it and suppose P1 itself gets the lock again and tries to produce another item. Since item 2 is still in the box, P1 calls `wait()`. Now P2 acquires the lock, finds that the box is full and also calls `wait()`. At this point, C1 and C2 are also waiting, while item 2 is still present in the box. All four threads are now in WAITING state, and nobody is left to consume the item or produce another one. This happens because `notify()` wakes up an arbitrary waiting thread, which might not be the thread that can actually make progress. To solve this, we use `notifyAll()` instead, which wakes up all waiting threads. They will then compete to acquire the lock one at a time and recheck their conditions using the while loop. The thread whose condition is satisfied will proceed, while the others will go back to waiting. This prevents the particular liveness problem caused by waking up the wrong type of thread.

        //Also its nots necessary to that wait will always put in permanent waiting state, we have a spurios wakeup problem where a threaed can wake up because OS+CPU decided because of some reason say internal optimization. Therefore we always use a while loop to check wait() instead of a if. Since if a thread wakes up spuriosly, it will again check the condition and go back to waiting.

        // Every object in java will have a lock, the lock will have certain properties like ownerthread, waiting queue, blocked queue etc. When a thread calls wait() method, it will release the lock and go into waiting queue. When another thread calls notify() method, it will wake up one of the waiting threads and put it in blocked queue if it cannot acquire the lock immeietely. The thread in blocked queue will wait until it can acquire the lock again.

    }
}

class Box {
     Integer item;
     boolean flag;
    synchronized void producer(Integer val) {
        while(item != null) {
            //do nothing
            try {
                wait();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        item = val;
        flag = true;
        System.out.println("Producer Produced: " + item);
        notify();
    }
    synchronized void consumer() {
        while(item == null) {
            //do nothing
            try {
                wait();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        System.out.println("Consumer Consumed: " + item);
        item = null;
        flag = false;
        notify();
    }
}
