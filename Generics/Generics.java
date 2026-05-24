import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generics {
    public static void main(String[] args) {
        //why Generics? because of type safety and no manual type casting and no compile time checkings   
        ArrayList list = new ArrayList();
        list.add("Hello");
        list.add(124);
        list.add(24.42);
        Object o = list.get(0); // but we want the string to we have to cast
        String s = (String) list.get(0);
        Integer i = (Integer) list.get(1); //this is not safe if by mistake we cast it to String instead of Integer it will thow ClassCastException at runtime.

        //how generics solves this 
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("Hello");    
        //stringList.add(25); // this will give compile time error as we cannot add Integer to a list of Strings. Type Safety achieved

        //Generic class example
        Box<Integer> box1 = new Box<>();
        box1.set(123);
        System.out.println(box1.get());
        Box<String> box2 = new Box<>();
        box2.set("Hello Generics");
        System.out.println(box2.get());

        Pair<String, Integer> pair = new Pair<>("Ram", 15);
        System.out.println(pair.getKey() + " is " + pair.getValue() + " years old.");

        //Convention of writing T for type, E for element, K,V for key-value objects, N for number.
        //Generic interfaces and classes 
        StringImplementation stringImpl = new StringImplementation();
        stringImpl.add("Hello Interface");
        System.out.println(stringImpl.get());
        //OR
        GenericClass<Double> genericImpl = new GenericClass<>();
        genericImpl.add(24.42); 
        System.out.println(genericImpl.get());

        //Bounded type parameters
        BoundedBox<Integer> intBox = new BoundedBox<>();
        //BoundedBox<String> doubleBox = new BoundedBox<>(); this will throw compile time error as string is not subsclass of Number as we have written in the class itsef T extends Number meaning T can only be of Number and its subclasses. This is also called upper bounded type parameter.

        //Generic constructor 
        Box3 box3 = new Box3("Hello Generic Constructor");
        Box3 box4 = new Box3(123);

        //generic methods
        Integer[] intArr = {1, 2, 3, 4, 5};
        String[] strArr = {"Hello", "Generics"};    
        printArr(intArr);
        printArr(strArr);

        //Wildcards in Generics, we can use ? as a wildcard to represent an unknown type. It can be used in method parameters, return types, and variable declarations. It is used when we want to specify that a method can accept any type of collection or when we want to specify that a method can return any type of collection. It is also used when we want to specify that a method can accept a collection of a specific type or its subclasses. For example, List<? extends Number> means that the method can accept a list of any type that is a subclass of Number. List<? super Integer> means that the method can accept a list of any type that is a superclass of Integer. List<?> means that the method can accept a list of any type. Can be used in method arguements or class definitions to represent an unknown type. Allow for more flexible or dynamic code by letting the type specified later or be more loosely defined.  
        //when we do read only tasks and not return anything we and use wildcards. 
        List<Integer> list3 = Arrays.asList(1, 2, 3, 4, 5);
        printList(list3);
        List<?> list4 = new ArrayList<String>();
        //list4.add(4); this will throw compile time error as it would not let you add sometinh 

        //wildcard extends and super
        List list5 = Arrays.asList(1,2.2,3.673,45);
        System.out.println(sum(list5));
        List<? extends Number> list6 = Arrays.asList(1, 2, 3, 4, 5);
        List<? super Integer> list7 = Arrays.asList(1, 2, 3, 4, 5);
        // list7.add(5.5);
        // list6.add(5.5);
        // printNumber(list6); this will throw error as we cannot add anything to type of unknown

        //TYPE ERASURE: Generics in Java are implemented using type erasure, which means that the generic type information is removed at runtime. This allows for backward compatibility with older versions of Java that do not support generics. However, it also means that we cannot use certain features of generics, such as creating instances of generic types or using instanceof with generic types. For example, we cannot create an instance of a generic type like new Box<T>() because the type information is erased at runtime. We can only create instances of specific types like new Box<Integer>() or new Box<String>(). Similarly, we cannot use instanceof to check if an object is an instance of a generic type like if (obj instanceof Box<T>) because the type information is erased at runtime. We can only check for specific types like if (obj instanceof Box<Integer>) or if (obj instanceof Box<String>) Java removed <> during compilation so the generic type information is not available at runtime List<Integer> becomes List during runtime and say int a = list.get() becomes int a = (Integer) list.get() during runtime. If only T then we get Object at runtime, if T extends something, we get that something at runtime 

        //Because of the above process, Java doesnt allow for generic type exceptions since during runtime, the type info is not available. So best is to use specific types in catch blocks instead of generic types.
        //But we do have a workaround is using a generic methods inside a specific exception class. See below

        //Understanding exceptions with generics  
        try {
            throw new MyExcetion(125);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            throw new MyExcetion("I am String");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //method for understanding wildcards where read only tasks are performed  
     public static void printList(List<?> list) {
            for(Object element : list) {
                System.out.print(element + " ");
            }
            System.out.println();
    }
    //but for write operations we cannot use wildcards because we don't know the type of the list and we cannot add anything to it. better to use 
    //public static <T> addList(List<T> list, T element) {
    //    list.add(element);}) instead of  
     /*public static void addToList(List<?> list) {
        list.add(1); // this will give compile time error as we cannot add anything to a list of unknown type. 
    }*/ 

    // wildcard extends something. extends Number is called upper bound as we can use only those types equal or less than Number or that extends Numbers like Integer, Double, Long etc. LIKE CEIL
    public static double sum(List<? extends Number> list) {
        double sum = 0;
        for(Number n : list) {
            sum += n.doubleValue();
        }
        return sum;
    } 

    //wildcard super something. LIKE FLOOR
    public static void printNumber(List<? super Integer> list) {
        for(Object element : list) {
            System.out.print((int) element + " ");
        }
        System.out.println();
    }



    //this is how to write a generic method, we have to write <T> before the return type of the method and then we can use T as a type parameter in the method.  
    public static <T> void printArr(T[] arr) {
        for(T element : arr) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
}

//this is generic class with type  T which can be defined while creating the instance of the class.
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
//with multiple type parameters
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

//generic interfaces
interface GenericInterface<T> {
    void add(T item);
    T get();
}
//class can implement generic interface 2 ways, by directly providing type or making the class itself generic
class StringImplementation implements GenericInterface<String> {
    private String item;

    @Override
    public void add(String item) {
        this.item = item;
    }

    @Override
    public String get() {
        return item;
    }
}
//or class can be generic
class GenericClass<T> implements GenericInterface<T> {
    private T item;

    @Override
    public void add(T item) {
        this.item = item;
    }

    @Override
    public T get() {
        return item;
    }
}

//Bounded type paramenters
class BoundedBox<T extends Number> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
//multiple bounds 
interface Printable {
    void print();
}
//we can can multiple bounds but class should be first then N number of interfaces but no interface first because class can only extend one class but can implement multiple interfaces.
class Box2<T extends Number & Printable> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
//lets focus on Generics constructors and methods 
class Box3 {
    public <T> Box3(T item) {
        System.out.println("Item: " + item);
    }
}

//Generics in Exceptions with workaround
class MyExcetion extends Exception {
    public <T> MyExcetion(T value) {
        super("Exception related to value: " + value.toString() + " of type: " + value.getClass().getName());
    }
}