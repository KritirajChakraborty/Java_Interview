import java.util.*;

public class UnderstandingIterator {
    public static void main(String[] args) {
        //Iterator is an interface that provides a way to access the elements of a collection sequentially without exposing the underlying implementation. It has three methods: hasNext(), next(), and remove(). It is used to traverse the elements of a collection and perform operations on them. It is fail-fast, which means that if the collection is modified while iterating, it will throw a ConcurrentModificationException. It is not thread-safe, so it should be used in a single-threaded environment or with proper synchronization.

        //ListIterator is an interface that extends the Iterator interface and provides additional methods to traverse the elements of a list in both directions (forward and backward). It has methods like hasPrevious(), previous(), nextIndex(), previousIndex(), set(), and add(). It is also fail-fast and not thread-safe.

        //Enumeration is an interface that provides a way to access the elements of a collection sequentially. It has two methods: hasMoreElements() and nextElement(). It is an older interface and is not fail-fast. It is not thread-safe, so it should be used in a single-threaded environment or with proper synchronization.

        //Iterator and ListIterator are part of the Java Collections Framework, while Enumeration is part of the legacy collections framework. Iterator and ListIterator are more commonly used in modern Java programming, while Enumeration is mostly used for legacy code.

        //Example of using Iterator
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        /* 
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        //Example of using ListIterator
        ListIterator<Integer> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }
        while (listIterator.hasPrevious()) {
            System.out.println(listIterator.previous());
        }
        */
        //for-each internally uses Iterators to iterate only  
        for(int i : list) {
            //if(i % 2 == 0) list.remove(Integer.valueOf(i)); //this will throw ConcurrentModificationException because we are modifying the list while iterating it.
            System.out.println(i);
        }
        //but we can use Iterator to remove from the list while iterating it.
        Iterator<Integer> iterator = list.iterator();
        while(iterator.hasNext()) {
            int i = iterator.next();
            if(i % 2 == 0) iterator.remove();
        }
        System.out.println(list);

        //ListIterator can be used to add elements to the list while iterating it. It has more methods than Iterator, we and traverse both sides and modify elements while iterating it. 
        
    }
}
