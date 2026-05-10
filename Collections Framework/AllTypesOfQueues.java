import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class AllTypesOfQueues {
   public static void main(String[] args) {

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


        //Deque also known as double ended queue which allows add and remove from both ends. Can be implmented using LL or ArrayDeque. Not thread safe and does not allow null values.

        Deque<Integer> dq = new ArrayDeque<>();  //new LinkedList<>() but slow
        //also here the internal array is circular, when head == tail, then size increases  
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

        //we can do all the abov steps with a LL but it would not be efficient as iteration is slower, more memory due to storing next pointers. 

        //All the above queues are not thread-safe 




   } 
}
