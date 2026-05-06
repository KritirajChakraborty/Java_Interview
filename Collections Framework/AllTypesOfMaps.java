import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class AllTypesOfMaps {
    public static void main(String[] args) {
        //Map is not part of Iterable and Collection interfaces. It is a separate interface that represents a collection of key-value pairs.  


        //HashMap is a map implementations that uses a hash table to store key-value pairs. Order of insertion is not preserved and only one null key is allowed. if you put another null key, the first will be overwritten. values can be null and keys cannot be duplicate. put,get,containsKey,remove all are O(1) on average but can degrade to O(n) in worst case if there are many hash collisions. HashMap is not synchronized and is not thread-safe.  
        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 25);
        map.put("Bob", 30);
        map.put("Lorna", 35);
        map.put(null,45);
        map.put(null,50); // This will overwrite the previous null key entry
        map.put("Boby",map.getOrDefault("Boby",25) + 21);
        map.remove(null);
        boolean didRemove = map.remove("Boby",15);
        System.out.println("Did remove Boby with age 15: " + didRemove);

        boolean hasAlice = map.containsKey("Alice"); 
        boolean has25Age = map.containsValue(25);

        System.out.println("HashMap: " +map);
        System.out.println("Has Alice: " + hasAlice);
        System.out.println("Has age 25: " + has25Age);

        //Iteration
        Set<String> keys = map.keySet();
        for(String key : keys) {
            System.out.println("Key: " + key + ", Value: " + map.get(key));
        }       

        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        for(Map.Entry<String, Integer> entry : entries) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            entry.setValue(entry.getValue() + 25);
        }
        System.out.println("Updated HashMap: " + map);

        HashMap<Integer,Integer> newMap = new HashMap<>(20,0.7f); // initial capacity is 20 and load factor is 0.7. This means that when the number of entries exceeds 70% of the capacity (i.e., 14 entries), the HashMap will resize itself by doubling the number of buckets and rehashing all existing entries to distribute them across the new buckets. This resizing operation can be costly in terms of performance, so it's important to choose an appropriate initial capacity if you know the expected number of entries in advance.

        //INTERNAL WORKING OF HASHMAP
        //HashMap uses an array of buckets to store key-value pairs. Each bucket can contain a linked list or a balanced tree (after Java 8) to handle hash collisions. When you put a key-value pair into the HashMap, it calculates the hash code of the key and determines the appropriate bucket index. If there is a collision (i.e., another key has the same hash code), it will store the new key-value pair in the same bucket using a linked list or tree structure. When retrieving a value, it calculates the hash code of the key, finds the corresponding bucket, and then searches through the linked list or tree to find the matching key and return its value. by default the initial capacity of the HashMap is 16 and the load factor is 0.75, which means that when the number of entries exceeds 75% of the capacity, the HashMap will resize itself by doubling the number of buckets and rehashing all existing entries to distribute them across the new buckets. This resizing operation can be costly in terms of performance, so it's important to choose an appropriate initial capacity if you know the expected number of entries in advance. threshold to convert from LL to RB-Tree in 8. Then searching will take O(log n) instead of O(n) in case of LL in worst case.

        //problem with hashmap is if we use custom object as keys, we need to override hashCode() and equals() methods to ensure that the HashMap can correctly identify keys and handle collisions. If we don't override these methods, the default implementation from the Object class will be used, which may lead to unexpected behavior when using custom objects as keys in the HashMap. For example, two different instances of a custom class with the same content may be treated as different keys if hashCode() and equals() are not properly implemented, resulting in duplicate entries or retrieval failures.

        Map<Student, String> studentMap = new HashMap<>();
        Student s1 = new Student("Alice", 20);
        Student s2 = new Student("Alice", 20);
        Student s3 = new Student("Bob", 22);
        studentMap.put(s1, "Student 1");
        studentMap.put(s2, "Student 2"); // This will be treated as a different key if hashCode() and equals() are not overridden
        //now after overriding check output again 
        studentMap.put(s3, "Student 3");
        System.out.println(studentMap.size());
        System.out.println(studentMap);

        //LINKEDHASHMAP is a hash table and linked list implementation of the Map interface, with predictable iteration order. It maintains a doubly-linked list running through all of its entries, which defines the iteration order. This means that when you iterate over the keys, values, or entries of a LinkedHashMap, they will be returned in the order in which they were inserted. Like HashMap, it allows one null key and multiple null values. The time complexity for basic operations (put, get, remove) is O(1) on average, but it can degrade to O(n) in worst case if there are many hash collisions. LinkedHashMap is not synchronized and is not thread-safe. 

        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>(5,0.6f,true); // accessOrder is false means it will maintain insertion order. if it is true then it will maintain access order.
        linkedHashMap.put("Alice", 25);
        linkedHashMap.put("Bob", 30);
        linkedHashMap.put("Lorna", 35);
        linkedHashMap.put("Boby", 45);
        linkedHashMap.get("Alice"); // Accessing Alice to make it most recently used if accessOrder is false then it will not change the order of the entries. This helps in making LRU cache implementation.
        System.out.println("LinkedHashMap: " + linkedHashMap);

        HashMap<String, Integer> map1 = new HashMap<>();
        map1.put("Alice", 45);
        LinkedHashMap<String, Integer> linkedHashMap1 = new LinkedHashMap<>(map1);
        linkedHashMap1.put("KC",25);
        System.out.println(linkedHashMap1);


        //INTERNAL WORKING OF LINKEDHASHMAP
        //LinkedHashMap uses a hash table to store key-value pairs, similar to HashMap but with an additional doubly-linked list to maintain the order of entries. Each entry in the LinkedHashMap contains a reference to the next and previous entries, allowing it to maintain the order of insertion or access. When you put a key-value pair into the LinkedHashMap, it calculates the hash code of the key and determines the appropriate bucket index, just like HashMap. However, it also updates the linked list to maintain the order of entries. When retrieving a value, it calculates the hash code of the key, finds the corresponding bucket, and then searches through the linked list to find the matching key and return its value. The linked list allows LinkedHashMap to maintain a predictable iteration order while still providing efficient access to key-value pairs. But this causes more memory overhead compared to HashMap due to the additional linked list structure. But get/put and remove are still O(1) on average.  



        // STRONG REFERENCE VS WEAK REFERENCE  
        Student strongStudent = new Student("Charlie", 24); // This is a strong reference. The object will not be garbage collected as long as this reference exists.
        WeakReference<Student> weakStudent = new WeakReference<>(new Student("Dave", 26)); // This is a weak reference. The object can be garbage collected when there are no strong references to it.
        System.out.println(strongStudent);
        System.out.println(weakStudent.get());   
        System.gc(); //try without this  
        
        /* 
        try {
            Thread.sleep(10000);
        } catch (Exception ignoredException) {
            // TODO: handle exception
        }
        */

        System.out.println(strongStudent);
        System.out.println(weakStudent.get());    // at this point not getting garbage collected because it is eligible for collection but JVM has not decided yet. Since the program is too small and there is less memory pressure, it wont work. So we force GC. See above in lin 86  

        //WEAKHASHMAP is a hash table-based implementation of the Map interface with weak keys. In a WeakHashMap, the keys are stored as weak references, which means that if a key is no longer in ordinary use (i.e., there are no strong references to it), it can be garbage collected. When a key is garbage collected, its corresponding entry in the WeakHashMap will also be removed. This makes WeakHashMap useful for caching purposes. GC will not work if keys are strong references like String literal because they are interned and have strong references from the string pool. So we need to use new String() to create a new object in heap which can be garbage collected. Same for integer, we can use Integer.valueOf() for values between -128 and 127 because they are cached and have strong references. For values outside this range, new Integer() will create a new object in heap which can be garbage collected. So we can use a higher number of Integer wrapper.

        WeakHashMap<Integer, Student> weakHashMap = new WeakHashMap<>(); 
        weakHashMap.put(150, new Student("Eve", 28)); 
        weakHashMap.put(250, new Student("Frankia", 21));
        System.out.println("WeakHashMap before GC: " + weakHashMap);
        //System.gc(); // Force garbage collection to see the effect of weak references
        /* 
        try {   
            Thread.sleep(10000); // Sleep to give GC time to run
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("WeakHashMap after GC: " + weakHashMap); // The entries may be removed if the keys were garbage collected
        */


        //IDENTITYHASHMAP is a hash table-based implementation of the Map interface with reference-equality semantics. In an IdentityHashMap, keys are compared using the == operator instead of the equals() method. This means that two keys are considered equal only if they are the same object in memory, not just if they have the same content. IdentityHashMap allows null keys and values, and it does not maintain any order of its entries. The time complexity for basic operations (put, get, remove) is O(1) on average, but it can degrade to O(n) in worst case if there are many hash collisions. IdentityHashMap is not synchronized and is not thread-safe.

        IdentityHashMap<String, Integer> identityHashMap = new IdentityHashMap<>();
        HashMap<String, Integer> hashMap = new HashMap<>();
        String key1 = new String("key");
        String key2 = new String("key");
        hashMap.put(key1, 1);
        hashMap.put(key2, 2); // This will overwrite the previous entry because key   
        identityHashMap.put(key1, 1);
        identityHashMap.put(key2, 2); // This will be treated as a different key because key1 and key2 are different objects in memory since in indentityHashMap keys are hashcoded using the Object's hascode method not the classses method an then compared using == operator which checks if the memory address are same. So both entries will be stored in the map. If we had used a normal hashmap then only one value would be stored. 

        System.out.println(key1.equals(key2));
        System.out.println(key1 == key2);
        System.out.println(System.identityHashCode(key1));
        System.out.println(System.identityHashCode(key2));
        System.out.println("IdentityHashMap: " + identityHashMap);
        System.out.println("HashMap: " + hashMap);



        //COMPARABLE:- we use it when we want a class to sort based on a single attribute. It is way to tell the natural ordering of sorting of a specific object based on single attribute to a List.

        List<Integer> list = new ArrayList<>(List.of(9,6,5,4,4,19,2));
        list.sort(null);
        System.out.println(list); //this is work but 

        List<Student> students1 = new ArrayList<>();
        students1.add(new Student("Alice",25));
        students1.add(new Student("Boby", 31));
        students1.add(new Student("Charlie", 17));
        students1.sort(null); // this will throw an error. Wont without implementing Comparable in the Student class to let it know on which attribute basis it should sort
        System.out.println(students1);


        // SORTEDMAP interface that implements TREEMAP where entries are sorted based on keys, either on their natural order or by a specific comparator. If we use a custom object as a key, we need to implement the comparable in class or give our customComparator in constructor. TreeMap is implemented using a Red-Black Tree aka Self Balancing Binary Search Tree. Hence all put/get/remove/containsKey are O(log n) and containsValue is O(n) since it will do full treversal

        SortedMap<Integer, String> sortedMap = new TreeMap<>();
        sortedMap.put(1,"Raju");
        sortedMap.put(24,"Rohan");
        sortedMap.put(4,"Rahul");
        sortedMap.put(14,"Kritiraj");
        sortedMap.put(3,"Kashi");
        sortedMap.put(13,"Kauwa");

        System.out.println("sortedMap: " + sortedMap);
        // we could have used a Map<Integer,String> too but if we dont use SortedMap<> then we loose some of the methods below
        System.out.println("FirstKey: " + sortedMap.firstKey());
        System.out.println("LastKey: " + sortedMap.lastKey());
        System.out.println(sortedMap.headMap(14)); // excludes the given key
        System.out.println(sortedMap.tailMap(14)); //includes
        System.out.println(sortedMap.subMap(1, 4)); //2nd param exclude
        System.out.println(sortedMap.subMap(13, 24)); // 2nd param exclude

        //NavigableMap is also an interface that implements TreeMap. Like before in SortedMap we had some methods that gave us an range from a -> b or from a or till b. Here in NavigableMap we have methods that help us find strictly lower/higher than give value

        NavigableMap<Integer,String> navigableMap = new TreeMap<>();
        navigableMap.put(1,"Raju");
        navigableMap.put(24,"Rohan");
        navigableMap.put(4,"Rahul");
        navigableMap.put(14,"Kritiraj");
        navigableMap.put(3,"Kashi");
        navigableMap.put(13,"Kauwa");

        System.out.println("NavigableMap: " + navigableMap);
        System.out.println("Lower Key: " + navigableMap.lowerKey(13)); //<
        System.out.println("Higher Key" + navigableMap.higherKey(13)); //>
        System.out.println("Ceiling Key" + navigableMap.ceilingKey(13)); // <=
        System.out.println("Floor Key" + navigableMap.floorKey(13)); // >=
        System.out.println(navigableMap.descendingMap());


        //HASHTABLE. It is a legacy class before CollectionsFramework came. It also extends Map and has same methods. But is is synchronized, does not accept null in key or value, slower than HashMap because of synchronization safety, also all methods are synchronised too (bad). We use ConcurrentHashMap instead of this in real time.

        Hashtable<Integer, String> hashtable = new Hashtable<>();
        ConcurrentHashMap<Integer,String> concurrentHashMap = new ConcurrentHashMap<>(); // using it to test the 2 threads below. Theory is below thread exercise
        /* 
        
        Thread t1 = new Thread(() -> {
            try {
                for(int i = 0; i < 10000; i++) {
                    // hashtable.put(i, "Thread 1");
                    concurrentHashMap.put(i,"Thread 1");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.getStackTrace();
            }
        });
         Thread t2 = new Thread(() -> {
            try {
                for(int i = 10000; i < 20000; i++) {
                    // hashtable.put(i, "Thread 2");
                    concurrentHashMap.put(i,"Thread 2");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.getStackTrace();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
         

        // System.out.println(hashtable.size());
        System.out.println(concurrentHashMap.size());
        */


        //CONCURRENTHASHMAP:- modern way of using thread-safe and synchronous maps. Till Java 7- it used segment based locking. 16 segments -> smaller hashmaps. Only the segment being written is locked whereas read do not require locking unless there is a write ops happening on that segment. 

        //From Java 8- no segmentation because of 16 segments only, it was not scalable. Hence came Compare and Swap Approach -> no locking except on resizing or collisions. 
        //FOR WRITE
        // Consider  Thread A :- last saw x -> 42
        //           Thread A :- x -> 50
        //          if x is still 42 then change to 50, else dont change and retry. Retry means again see what value is there currently say(43) then check if 43 then put 50 else retry
        // FOR READING: no locking is there. 

        // locking happens during resizing because suppose we add a new bucket and now 2 threads want to simultaneously work on it. hence its locked. Unlike regular hashmaps where load factor is 0.75f, here we have incremental load factor mean + 2/x buckets
        // locking during collision is done because there we have to check a LinkedList and there are multiple pointers prev/next and multiple threads can access it hence locking is required

        //can do the above same exercise of 2 threads with ConcurrentHashMap



        //CONCURRENTSKIPLISTMAP  :- It is like asynnchronised and sorted map. Thread-safe TreeMap. But it doesnot implement a RB-tree rather it uses a SkipList. A SkipList is a probabilistic data structure that allows efficient search,insertion and removal ops
        //It is similar to a sorted LL but with multiple layers that SKIP over some portions of the list to provide faster access. See below

        /*
          1----------------5---------------9
          1-------3--------5-------7-------9
          1---2---3---4----5---6---7---8---9
         Here say if we want to find 2. we check the top layer where we go from 1 -> 5 but 2 is less so we go to the next layer and 1 -> 3,again 2 is less and then we go the 3rd layer and find 2.
        */
        
        ConcurrentSkipListMap<Integer,String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        //it has all methods from NavigableMap since it extends ConcurrentNavigable map which extends ConcurrentMap and NavigableMap
        //Above exercise can done with this also



        //ENUMMAP :- it takes a enum value as a key and enum.class as param in constructor. internally it is an array because enum size is also fixed. hence faster than hashmap. no hashing is involved here. Ordinal.index is used
        //["Gym",_,_,_,"Running",_,_]

        Map<Day, String> enumMap = new EnumMap<>(Day.class);
        enumMap.put(Day.MONDAY, "Gym");
        enumMap.put(Day.FRIDAY, " Running");
        System.out.println(enumMap);


        //IMMUTABLEMAP:- where values can be read but not modified

        Map<Integer,String> map3 = new HashMap<>();
        map3.put(1,"Raja");
        map3.put(2,"Rohan");
        Map<Integer, String> map4 = Collections.unmodifiableMap(map3);
        map4.get(2); //is ok
        //map4.put(3,"Kriti"); //this will throw error
        //but this can be bipassed by modifying the original map
        map3.put(3,"Kriti"); //this wont throw error
        System.out.println("Map3: " + map3);
        System.out.println("Map4: " + map4);

        Map<Integer,String> map5 = Map.of(1,"Rohan", 2 , "Rohit"); // but this has an issue that it can hold only 10 values max

        Map<Integer,String> map6 = Map.ofEntries(Map.entry(1, "Rohan"),Map.entry(2, "Rohit")); // this is scalable

        System.out.println("Map5: " + map5);
        System.out.println("Map6: " + map6);
        //map5.put(4,"ABC"); throws error:- UnsupportedOperationException
    }
 
}

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

class Student implements Comparable<Student> {
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    // /*first check without ovverriden methods   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
    //*/

    //we also need to override toString() method to print the student details in a readable format otherwise it will print the address  
    @Override
    public String toString() {
        return "{name : " + this.name + ", age: " + this.age + "}";
    }

    //we have to implement the compareTo method when we make a class implement Comparabe
    @Override
    public int compareTo(Student o) {
        return Integer.compare(this.age,o.age); // better than this.age - o.age because of no  overflow
    }
}