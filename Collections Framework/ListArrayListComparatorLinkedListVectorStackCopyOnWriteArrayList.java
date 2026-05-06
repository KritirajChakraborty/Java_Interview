import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListArrayListComparatorLinkedListVectorStackCopyOnWriteArrayList {
    public static void main(String[] args) {

        System.out.println("The is the beginning of Collections Framework!");

        /*Iterable is the root interface. Collection is a sub interface which extends Iterable. List, Set and Queue are sub interfaces which extends Collection. ArrayList, LinkedList, HashSet, TreeSet, PriorityQueue are the classes which implements List, Set and Queue interfaces. */

        // List interface and ArrayList class

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(5);
        list.add(3); // .add method adds element to the last of list
        System.out.println(list.size()); //size returns the size of the list
        //capacity is the initial size of the list which is 10 by default and increases 1.5 times when list becomes full. We cannot access capacity but can check by using Reflection API.
        System.out.println(list.get(0)); //returns the element at the specified index
        list.remove(1); //removes the element at the specified index
        list.remove(Integer.valueOf(3)); //removes the first occurrence of the specified element from the list. here we have to use Integer wrapper otherwise if we use int, then that index is removed. Internally remove expects (int e, Ob o)  
        System.out.println(list.contains(3)); //returns true if the list contains the specified element
        //list.clear(); //removes all the elements from the list
        list.add(1,55); // adds the element 2nd param at the index 1st param
        list.set(1, 100); // replaces the element at the specified index with the specified element
        System.out.println(list.isEmpty()); // returns true if the list is empty

        System.out.println(list);  
        for(int i : list) {
            System.out.println(i + " ");
        }

        List<Integer> arr = Arrays.asList(1,2,3,4,5); 
        //this returns an list whose size is fixed but existing elements can be modified. 
        System.out.println(arr);
        arr.set(2,50); // this will change the element at index 2 to 50. But we cannot add or remove elements from this list as it is backed by an array which has fixed size. So arr.add(6) will throw UnsupportedOperationException.
        System.out.println("Type of arr: " + arr.getClass().getName()); //returns Arrays$ArrayList which is a private static class inside Arrays class which implements List interface. So we cannot use ArrayList<Integer> here.
        List<Integer> arr2 = new ArrayList<>(arr); // this creates a new ArrayList with the elements of arr. Now we can modify arr2 as it is a separate list.
        arr2.add(6); // this will add 6 to the end of arr2
        System.out.println("Modified arr1 in arr2: " + arr2);

        List<String> list1 = List.of("Apple", "Banana", "Cherry"); // this returns an immutable list which means we cannot add, remove or modify elements in this list. This method is available from Java 9 onwards. Here we cannot modify also.
        System.out.println(list1.getClass().getName());

        //but how to modify the above list 
        List<String> list2 = new ArrayList<>(list1); // () this takes a collections as an arguement or initial capacity eg 1000.  
        list2.add("Mango"); //so list1 kinda is immutable but we can copy and modify in a new list
        System.out.println(list2);

        // how adding and removing works is first capacity is checked if full, new array of 1.5 times current size is created and elements are copied to new array amd then new element is added. When removing an element, the elements after the removed element are shifted to left by one position and last element is set to null. if removing decreases the size to less than 25% of the capacity, new array of half the current size is created and elements are copied to new array. This is how ArrayList maintains its size and capacity.

        // TC of add, remove, get, set, contains is O(1) on average but can be O(n) in worst case when resizing is needed. Size and isEmpty are O(1). Iterating through the list is O(n).

        //all the above methods can be used by ArrayList too as it implements List interface 




        // Comparator:- It is a interface which is used to compare two objects of the same class. It has a method compare which returns a negative integer, zero or a positive integer as the first argument is less than, equal to, or greater than the second. It is used in sorting and searching algorithms. We can use it to sort a list of objects based on a specific field. For example, we can sort a list of students based on their marks.

        List<Integer> list4 = new ArrayList<>(Arrays.asList(5,2,8,1,4));
        List<String> list5 = new ArrayList<>(Arrays.asList("Ali","Amit", "Amat", "Rohan", "Danish", "Rahul","Bob Marley"));
        System.out.println(list4);
        System.out.println(list5);
        List<String> list6 = new ArrayList<>(list5);
        List<Integer> list7 = new ArrayList<>(list4);

        //Collections.sort(list4);
        list5.sort(null); // natural order/ asc / Comparator.naturalOrder()
        list4.sort(Comparator.reverseOrder()); // reverse order/desc 
        list6.sort(new myComparator()); // custom comparator which sorts based on length of string
        list7.sort((a,b) -> b - a);  


        System.out.println("Reverse order: " + list4);
        System.out.println("Sorted by Natural Order: " + list5);
        System.out.println("Sorted by custom comparator: " + list6);
        System.out.println("ReverseOrder using Lambda expression: " + list7 );

        List<Student> students = new ArrayList<>();
        students.add(new Student("David",3.5));
        students.add(new Student("Alice",3.5));
        students.add(new Student("Bob",3.8));
        students.add(new Student("Charlie",3.2));
       
        //this is lambda expression  
        /*  students.sort((a,b) -> {
            if(b.gpa - a.gpa > 0) {
                return 1;
            } else if(b.gpa - a.gpa < 0) {
                return -1;
            } else {
                return a.name.compareTo(b.name);
            }
        }); */

        //or
        //this is using custom comparator class  
        //students.sort(new myStudentComparator());
        //or
        //method reference This is using Comparator.comparing  
        Comparator<Student> comparator = Comparator.comparing(Student::getGpa).reversed().thenComparing(Student::getName);
        students.sort(comparator);
        Collections.sort(students, comparator);

        for(Student student : students) {
            System.out.println(student.name + " " + student.gpa);
        }



        //LINKEDLIST (java by default uses doubly linked list)
        //add/delete at head/tail is O(1) traverse/add/delete at middle O(n)  
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1); 
        linkedList.add(2);  
        linkedList.add(3);
        System.out.println(linkedList.get(1));
        linkedList.addFirst(0); //O(1) we have lly removeFirst() and removeFirstOccurance()  
        linkedList.addLast(4); //O(1) we have lly removeLast() and removeLastOccurance()
        linkedList.set(4,5); //O(n) as we have to traverse the list to find the index and then set the value
        linkedList.add(4,4);
        System.out.println(linkedList);

        LinkedList<String> animals = new LinkedList<>(Arrays.asList("Dog", "Cat", "Elephant", "Lion", "Tiger"));
        LinkedList<String> animalsToRemove = new LinkedList<>(Arrays.asList("Cat", "Elephant"));
        System.out.println(animals);
        animals.removeAll(animalsToRemove);
        System.out.println("After removing: " + animals);

        linkedList.removeIf(x -> x%2 == 0); //lambda expression to remove even numbers  
        System.out.println("After removing even numbers: " + linkedList);

        //THIS IS FOR LINKEDLIST AS A LIST.  



        //VECTOR (legacy class, synchronized, thread-safe, slower than ArrayList because of sync overhead of locking and unlocking, use ArrayList instead in single threaded environment)
        //vector is also dynamic sizing but here we can access capacity and set increment size of capacity too. by default cap is 10 and increment is double but we can change it. 

        //Vector<Integer> vector = new Vector<>();
        ArrayList<Integer> vector = new ArrayList<>();
        //vector has all the methods of arraylist  
        Vector<String> vector2 = new Vector<>(Arrays.asList("Apple", "Banana", "Cherry"));
        Vector<Integer> vector3 = new Vector<>(3,2);
        vector3.add(1);
        vector3.add(2); 
        vector3.add(3);
        System.out.println(vector3.capacity()); 
        vector3.add(4);
        System.out.println(vector3.capacity()); 

        Thread t1 = new Thread(() -> {
            for(int i=0; i<1000; i++) {
                vector.add(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i=0; i<1000; i++) {
                vector.add(i);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            e.getStackTrace();
        }

        System.out.println(vector.size());
        vector.clear();



        //STACK (LIFO) based and synchronized, thread-safe because it extends Vector, slower than ArrayDeque because of sync overhead of locking and unlocking, use ArrayDeque instead in single threaded environment. Stack is a legacy class and it is recommended to use Deque interface and its implementation ArrayDeque for stack operations.

        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack);
        int top = stack.peek(); // returns the top element without removing it O(1)
        System.out.println(stack);
        int removedTopElement = stack.pop(); // removes and returns the top element O(1)
        System.out.println(stack);
        //stack has all methods of vector but we should not use them like get,remove etc  

        //LINKEDLIST AS A STACK
        LinkedList<Integer> stack2 = new LinkedList<>();
        stack2.addLast(1); 
        stack2.addLast(2);
        stack2.addLast(3);
        stack2.getLast(); // imitating peek() O(1)
        stack2.removeLast(); // imitating pop() O(1)    

        //ARRAYLIST AS A STACK is also possible because it has add but for peek and pop we have to use arr.size()-1. hence not recommended. 


        //COPYONWRITEARRAYLIST (thread-safe, slower than ArrayList because of copying entire array on every write operation, use ArrayList instead in single threaded environment, good for scenarios where read operations are more frequent than write operations)
        //Copy on write means whenever a write operations like adding or removing is performed, 
        //instead of directly modifying the existing arraylist,
        //a new copy of the list is created and modification is applied on that list
        //This ensured that other threads reading on the arraylist are unaffected
        //Read ops: fast and direct since theuy happen on stable list without interference or modifications
        //Write ops: slower because a new copy is made everytime 
        //            and the reference to the new list is updated so subsequent read ops will use the new list

        List<String> cowList = new CopyOnWriteArrayList<>(); // only ArrayList<>() will throw error  
        cowList.add("Item1");
        cowList.add("Item2");
        cowList.add("Item3");

        Thread readerThread = new Thread(() -> {
            try {
                while(true) {
                    for(String item : cowList) {
                        System.out.println("Reader Thread: " + item);
                        Thread.sleep(100); // slight delay to simulate time taken for reading and to allow writer thread to perform write operations
                    }
                    
                }
                
            } catch (Exception e) {
                e.getStackTrace();
            }
        });

        
        Thread writeThread = new Thread(() -> {
            try {
                Thread.sleep(500);
                cowList.add("Item4");
                System.out.println("Writer Thread: Added Item4");

                Thread.sleep(500);
                cowList.remove("Item1");
                System.out.println("Writer Thread: Removed Item1");
                
            } catch (Exception e) {
                e.getStackTrace();
            }
        });

        readerThread.start();
        writeThread.start();    

        try {
            readerThread.join();
            writeThread.join();
        } catch (Exception e) {
            System.out.println("Error occured in main thread" + e.getStackTrace());

        }
    }
}

//this will sort the string based on the length of string. We can also use lambda expression instead of creating a separate class for comparator. For example, list5.sort((s1, s2) -> s1.length() - s2.length());
class myComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length(); // s2.length - s1.length will be reverseOrder of length
    }
}


// Using this object to understand Comparator, lambda expression and method reference.  
class Student {
    String name;
    double gpa;
    public Student(String name, double gpa) {
        this.name = name;
        this.gpa = gpa;
    } 
    public String getName() {
        return name;
    }
    public double getGpa() {
        return gpa;
    }
}
class myStudentComparator implements Comparator<Student> {
    
    @Override
    public int compare(Student s1, Student s2) {
        if(s2.gpa - s1.gpa > 0) {
            return 1;
        } else if(s2.gpa - s1.gpa < 0) {
            return -1;
        } else {
            return s1.name.compareTo(s2.name);  
        }
    }
}