import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class AllTypesOfQueues {

    static final int max = 5000000; //to test throughput between CLQ and BQ
   public static void main(String[] args) throws InterruptedException {

        
        //LL can be used as a Queue. FIFO
        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(1); //adding in Queue is called enqueue
        queue.addLast(2);
        queue.addLast(3);
        System.out.println(queue);
        queue.removeFirst(); //removing in Queue is called dequeue
        System.out.println(queue);

        //But using LL as queue is not efficient as it has extra method not required for queue. So we have a Queue interface in java which is implemented by LinkedList and PriorityQueue class.
        Queue<Integer> q = new LinkedList<>();
        q.add(1);
        q.add(2);
        q.offer(3); //offer is also used to add in queue but it returns false if the queue is full, while add throws an exception.
        System.out.println(q);
        q.remove();
        q.poll(); //poll is also used to remove from queue but it returns null if the queue is empty, while remove throws an exception.
        q.peek(); //to get top element 
        q.element(); //to get top element but it throws an exception if the queue is empty, while peek returns null.
        System.out.println(q);

        //ArrayBlockingQueue is a bounded queue which means it has a fixed capacity. It is thread safe and is used in producer-consumer scenarios.
        Queue<Integer> q2 = new ArrayBlockingQueue<>(2);
        System.out.println(q2.add(1));
        System.out.println(q2.offer(2)); 
        //if we add using add, it will throw error but for offer it will return false if queue is full 
        System.out.println(q2.offer(3));

        //PriorityQueue is a queue which is based on priority. It is not thread safe and it does not allow null values. It is used when we want to process elements based on their priority. Natural order for primitives and for custom objects we can provide a comparator.
        PriorityQueue<Integer> q3 = new PriorityQueue<>();
        q3.offer(25);
        q3.add(3);
        q3.add(1);
        q3.add(2);
        q3.offer(0);
        System.out.println("q1 PQ: " + q3); // this will be different from looping with poll

        while(!q3.isEmpty()) {
            System.out.println(q3.poll()); // this will print in sorted order
        }   

        //it also has the same methods as Queue interface
        PriorityQueue<Integer> q4 = new PriorityQueue<>(Collections.reverseOrder());
        //OR
        PriorityQueue<Integer> q5 = new PriorityQueue<>((a, b) -> b - a); // for custom comparator but has risk of integer overflow so we use 
        PriorityQueue<Integer> q7 = new PriorityQueue<>((a, b) -> Integer.compare(b, a)); // this has no integer overflow
        //OR 
        PriorityQueue<Integer> q6 = new PriorityQueue<>(Comparator.reverseOrder()); // for custom comparator

        //Implementation of PQ in DSA notes heap, min-heap and max-heap. 


        //Deque also known as double ended queue which allows add and remove from both ends. Can be implmented using LL or ArrayDeque. Not thread safe and does not allow null values. Faster than stack when used as a stack as it does not have to deal with synchronization issues. And faster than LL when used as a queue as it does not have to deal with extra memory for storing next pointers.
        //when we remove elements from the start, we dont have to shift elements in case of Deque as the internal array is circular, when head == tail, then size increases. if we add at head, internally head-- and if we add at tail, internally tail++. when we remove from head, head++ and when we remove from tail, tail--. Hence when tail == head ,size doubles.

        Deque<Integer> dq = new ArrayDeque<>();  //new LinkedList<>() but slow
        //also here the internal array is circular, when head == tail, then size increases. But LL is better when insertion/deletion in middle is required.
        dq.addFirst(1); // adding at front but throws exception if it fails
        dq.offerFirst(2); // adding at front but returns false if it fails
        dq.addLast(45);
        dq.offerLast(41); //same rules as above 
        System.out.println("dq: " + dq);
        dq.removeFirst(); //removing from front but throws exception if it fails
        dq.pollFirst(); //removing from front but returns null if it fails   
        dq.removeLast(); //removing from end but throws exception if it fails
        dq.pollLast(); //removing from end but returns null if it fails
        System.out.println("dq: " + dq);
        dq.offerLast(1);
        dq.offerFirst(2);
        dq.offerLast(3);
        while (!dq.isEmpty()) {
            System.out.println(dq.pollFirst());
        }

        /* 
        System.out.println(dq.getFirst()); //throws ex if empty
        System.out.println(dq.peekFirst()); //returns null if empty
        System.out.println(dq.getLast()); //throws ex if empty
        System.out.println(dq.peekLast()); //returns null if empty
        dq.remove(Integer.valueOf(2));  
        */

        //we can do all the abov steps with a LL but it would not be efficient as iteration is slower, more memory due to storing next pointers. 

        //All the above queues are not thread-safe except ArrayBlockingQueue.

        //BLOCKING QUEUES
        //it is thread-safe
        //waits from queue to become non-empty / wait for space  
        //simplifies concurrency problems like producer-consumer  
        //it has methods like put, take, offer with timeout, poll with timeout.
        // in standard queues -> ops happen immediately. say if Q is empty and we cal remove or queue is full and we call add, it will throw exception.
        //in blocking queues -> we have 
            //put -> Block if the queue is full until space is available.
            //take -> Block if the queue is empty until an element is available.
            //offer -> waits for space to beome avaialable upto a specified timeout, returns false if timeout expires before space is available.
            //poll -> waits for an element to be available up to a specified timeout, returns null if timeout expires before an element is available.
        
        BlockingQueue<Integer> bq = new ArrayBlockingQueue<>(5);
        //it is a bounded queue backed by a circular array. Thread safe and does not allow null values. Used in producer-cosumer scanarios.  
        //uses a single lock for both enqueue and dequeue ops hence not very efficient when multiple thread are there. 
        /* 
        Thread producerThread = new Thread(new Producer(bq));
        Thread consumerThread = new Thread(new Consumer(bq));
        producerThread.start();
        consumerThread.start();
        */
        
        //LinkedBlockingQueue 
        BlockingQueue<Integer> bq2 = new LinkedBlockingDeque<>(5);
        //if can be optionally unbounded so if we dont give capacity then its capacity will be Integer.MAX_VALUE but if used could throw a OutOfMemoryError.
        //uses seperate locks for enqueue and dequeue ops and better for multiple thread scenarios. Same methods  

        //PriorityBlockingQueue
        BlockingQueue<Integer> bq3 = new PriorityBlockingQueue<>(); //def initial capacity is 11 
        //since it is unbounded so put will not block. Rest all functionalities same as PriorirtyQueue. All methods same as above blocking queues. Not thread safe for iteration as it is not locked during iteration.

        //PROBLEM IS THE ABOVE QUEUES ARE THREAD-SAFE BUT BLOCKING IN NATURE.
        //Thats why we have ConcurrentLinkedQueue and ConcurrentLinkedDeque which are non-blocking thread-safe queues. They use CAS operations for thread safety and are based on linked nodes. They do not block threads but may have higher latency under contention. They are used in scenarios where we want to avoid blocking and can tolerate some latency.

        ConcurrentLinkedQueue<Integer> clq = new ConcurrentLinkedQueue<>();
        //it is an unbounded thread-safe queue based on linked nodes. It does not allow null values. 

        //Similarly 
        ConcurrentLinkedDeque<Integer> cldq = new ConcurrentLinkedDeque<>();
        //it is a non-blocking thread-safe double ended queue

        //Understand the difference between ConcurrentLinkedQueue and BlockingQueue with an example below.
        testBlockingQueue();
        testConcurrentLinkedQueue();

   } 
   //example to understand the throughput diff between BlockingQueue and ConcurrentLinkedQueue. 
   public static void testBlockingQueue() throws InterruptedException {
    BlockingQueue<Integer> bq = new ArrayBlockingQueue<>(max);
    Thread producerThread = new Thread(() -> {
        for (int i = 0; i < max; i++) {
            try {
                bq.put(i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    });

    Thread consumerThread = new Thread(() -> {
        for (int i = 0; i < max; i++) {
            try {
                bq.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    });

    long startTime = System.currentTimeMillis();
    producerThread.start();
    consumerThread.start();

    producerThread.join();
    consumerThread.join();
    long endTime = System.currentTimeMillis();
    System.out.println("BlockingQueue time: " + (endTime - startTime) + " ms");
   }
   public static void testConcurrentLinkedQueue() throws InterruptedException {
    ConcurrentLinkedQueue<Integer> clq = new ConcurrentLinkedQueue<>();
    Thread producerThread = new Thread(() -> {
       for (int i = 0; i < max; i++) {
            try {
                clq.add(i);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                System.out.println(e.getStackTrace());
            }
        }
    });

    Thread consumerThread = new Thread(() -> {
         int count = 0;
         try {
             while (count < max) {
                Integer value = clq.poll();

                if (value != null) {
                    count++;
                }
            }
         } catch (Exception e) {
            System.out.println(e.getStackTrace());
             Thread.currentThread().interrupt();
         }
           
    });

    long startTime = System.currentTimeMillis();
    producerThread.start();
    consumerThread.start();

    producerThread.join();
    consumerThread.join();
    long endTime = System.currentTimeMillis();
    System.out.println("ConcurrentLinkedQueue time: " + (endTime - startTime) + " ms");
   }
}
//Producer-Consumer problem using BlockingQueue
class Consumer implements Runnable {
    private BlockingQueue<Integer> bq;

    public Consumer(BlockingQueue<Integer> bq) {
        this.bq = bq;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Consumer consumed: " + bq.take());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted");
            }
        }
    }
}
//Producer-Consumer problem using BlockingQueue
class Producer implements Runnable {
    private BlockingQueue<Integer> bq;
    private int val = 0;

    public Producer(BlockingQueue<Integer> bq) {
        this.bq = bq;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Producer produced: " + val);
                bq.put(val++); // this will block if the queue is full
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

