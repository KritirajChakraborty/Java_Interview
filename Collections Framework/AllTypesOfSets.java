import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;


public class AllTypesOfSets {
    public static void main(String[] args) {

        // Set is a collection that contains no duplicate elements. It is based on the Map interface and is implemented by the HashSet, LinkedHashSet, and TreeSet, EnumSet classes. So all add/remove/contains are O(1) for HashSet and LinkedHashSet, and O(log n) for TreeSet.
        //Internally set is a map only but instead of values, it stored dummy values. when we do map.keySet() on a map, we get a set. Because keys of a map are unique.
        //HashSet is unordered,LinkedHashSet is ordered and TreeSet is sorted.  
        //We have SortedSet and NavigableSet interdaces also impemented by TreeSet.  
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(25);
        set.add(1);
        set.add(14);
        System.out.println(set);
        //The above sets are not thread-safe but we can make them thread-safe by using Collections.synchronizedSet() method. But it is not efficient sice its methods are blocking so when one thread is working other threads will be blocked.  
        Set<Integer> set1 = Set.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15); //just like Map.of or List.of, it is immutable and does not allow null values. It is also thread safe.
        Set<Integer> set2 = Collections.unmodifiableSet(set); //it is also immutable but it allows null values and it is not thread safe. It is just a wrapper around the original set. So if we change the original set, the unmodifiable set will also change.
        Set<Integer> set3 = new ConcurrentSkipListSet<>();
        Set<Integer> set4 = new CopyOnWriteArraySet<>();

        //ConcurrentSkipListSet is a thread-safe variant of TreeSet. It is based on a skip list data structure and provides O(log n) time complexity for add/remove/contains operations. It is sorted and does not allow null values. Better for write-heavy workloads than CopyOnWriteArraySet because it does not create a new copy of the array for every write operation. It uses a lock-free algorithm to allow concurrent modifications by multiple threads.
        //CopyOnWriteArraySet is a thread-safe variant of HashSet. It is based on a copy-on-write array data structure and provides O(n) time complexity for add/remove/contains operations. It is unordered and does not allow null values. It is efficient for read-heavy workloads but not for write-heavy workloads. Because everytime we do write ops, it creates a new copy of the array and modifies the copy and after ops is done it replaces the original array with copy.  
        for(int i = 1; i < 6; i++) {
            set3.add(i);
            set4.add(i);
        }

        System.out.println("Modifying and Reading CopyOnWriteArraySet");

        for(int i : set4) {
            System.out.println(i);
            set4.add(i+5);
            if(i == 5) {
                set4.remove(1);
            }
        }
        System.out.println("CopyOnWriteArraySet: " + set4);

        System.out.println("Modifying and Reading ConcurrentSkipListSet");

        for(int i : set3) {
            System.out.println(i);
            //set3.add(i+5); // this will start a infinite loop. 
            if(i == 5) {
                set3.add(7);
            }
        }
        System.out.println("ConcurrentSkipListSet: " + set3);

    }
    
}
