public class InnerClasses {

    public static void main(String[] args) {

        /*
        Inner Classes Interview Notes

        There are 4 types of inner classes:

        1 Member Inner Class
            -> Belongs to outer object

        2 Static Nested Class
            -> Belongs to outer class

        3 Local Inner Class
            -> Belongs to a method

        4 Anonymous Inner Class
            -> No name, one-time use

        Most commonly used in real projects:
        Static Nested Class > Anonymous Class/Lambda > Member Inner Class > Local Inner Class
        */


        System.out.println("========== MEMBER INNER CLASS ==========");

        //Member Inner Class:
        //It means a class which is defined inside another class and outside any method.
        //It belongs to the instance of the outer class.
        //It can access all members of outer class including private members.
        //To create its object, we first need an object of outer class.

        Car car = new Car("Toyota");

        //Syntax:
        //Outer.Inner inner = outer.new Inner();

        Car.Engine engine = car.new Engine();

        engine.start();
        engine.stop();



        System.out.println("\n========== STATIC NESTED CLASS ==========");

        //Static Nested Class:
        //It is a nested class declared with static keyword.
        //Unlike member inner class, it does not belong to outer object.
        //It belongs to the class itself.
        //It can access only static members of outer class directly.
        //It can be instantiated without creating an object of outer class.
        //
        //Main Advantage:
        //It saves memory because it does not maintain a hidden reference
        //to outer class object.

        Computer computer =
                new Computer("HP", "Pavilion", "Windows 11");

        computer.displayInfo();

        //No outer object needed

        Computer.USB usb =
                new Computer.USB("Type-C");

        usb.displayInfo();



        System.out.println("\n========== LOCAL INNER CLASS ==========");

        //Local Inner Class:
        //It is declared inside a method.
        //Its scope is limited only to that method.
        //Cannot be accessed outside the method.
        //Can access:
        //1 Outer class members
        //2 Local variables which are final/effectively final

        BankAccount account = new BankAccount(5000);

        account.withdraw(1000);



        System.out.println("\n========== ANONYMOUS INNER CLASS ==========");

        //Anonymous Inner Class:
        //It is a class without a name.
        //Declared and instantiated at the same time.
        //Used for one-time implementation.
        //Cannot have explicit constructor.
        //Cannot be reused.

        ShoppingCart cart = new ShoppingCart(2500);

        cart.checkout(new Payment() {

            @Override
            public void pay() {
                System.out.println(
                        "Paid ₹" + cart.amount + " using UPI");
            }
        });



        System.out.println("\n========== ANONYMOUS CLASS WITH ABSTRACT CLASS ==========");

        Animal dog = new Animal() {

            @Override
            void sound() {
                System.out.println("Dog barks");
            }
        };

        dog.sound();
    }
}


/*
=========================================================
1 MEMBER INNER CLASS
=========================================================

Member Inner Class:
It means a class which is defined inside another class
and outside any method.

Important Points:
1 Belongs to outer class object.
2 Can access all members of outer class including private members.
3 Cannot be instantiated without outer class object.

Use Cases:
1 Car -> Engine
2 House -> Room
3 University -> Department

When to Use:
When inner object cannot logically exist without outer object.
*/

class Car {

    private String name;
    private boolean isEngineOn;

    public Car(String name) {
        this.name = name;
    }

    class Engine {

        void start() {

            if (!isEngineOn) {

                isEngineOn = true;

                System.out.println(
                        name + " engine started.");

            } else {

                System.out.println(
                        name + " engine already running.");
            }
        }

        void stop() {

            if (isEngineOn) {

                isEngineOn = false;

                System.out.println(
                        name + " engine stopped.");

            } else {

                System.out.println(
                        name + " engine already off.");
            }
        }
    }
}



/*
=========================================================
2 STATIC NESTED CLASS
=========================================================

Static Nested Class:
It is a nested class declared with static keyword.

Important Points:
1 Belongs to outer class itself.
2 Does not belong to outer object.
3 Can access only static members directly.
4 Can be instantiated without outer object.

Advantages:
1 Saves memory.
2 No hidden reference to outer object.
3 Useful for helper classes.

Real World Examples:
1 HashMap.Node
2 Builder Classes
3 Configuration Classes

When to Use:
When nested class is logically related
but does not need outer object's state.
*/

class Computer {

    private String brand;
    private String model;
    private String osName;

    private static int computerCount = 0;

    public Computer(
            String brand,
            String model,
            String osName) {

        this.brand = brand;
        this.model = model;
        this.osName = osName;

        computerCount++;
    }

    void displayInfo() {

        System.out.println(
                brand + " " +
                model + " running " +
                osName);
    }

    static class USB {

        private String type;

        public USB(String type) {
            this.type = type;
        }

        void displayInfo() {

            System.out.println(
                    "USB Type : " + type);

            System.out.println(
                    "Total Computers : "
                            + computerCount);

            //Cannot access brand/model here
            //because they are instance members.
        }
    }
}



/*
=========================================================
3 LOCAL INNER CLASS
=========================================================

Local Inner Class:
It is declared inside a method.

Important Points:
1 Scope limited to that method.
2 Cannot be accessed outside method.
3 Can access outer class members.
4 Can access final/effectively final local variables.

When to Use:
When helper logic is required only inside one method.

Examples:
1 Validation helper
2 Transaction logger
3 Parsing helper
*/

class BankAccount {

    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public void withdraw(double amount) {

        String transactionType = "WITHDRAWAL";

        class TransactionLogger {

            void log() {

                System.out.println(
                        transactionType +
                        " of ₹" +
                        amount +
                        " completed.");
            }
        }

        if (amount <= balance) {

            balance -= amount;

            TransactionLogger logger =
                    new TransactionLogger();

            logger.log();

            System.out.println(
                    "Remaining Balance = ₹"
                            + balance);
        }
    }
}



/*
=========================================================
4 ANONYMOUS INNER CLASS
=========================================================

Anonymous Inner Class:
It is a class without a name.

Important Points:
1 Created and instantiated at same time.
2 Used only once.
3 Cannot have explicit constructor.
4 Cannot be reused.

Before Java 8:
Very common for interfaces and callbacks.

After Java 8:
Mostly replaced by lambda expressions.

When to Use:
One-time implementation of interface or abstract class.
*/

interface Payment {

    void pay();
}

class ShoppingCart {

    double amount;

    public ShoppingCart(double amount) {
        this.amount = amount;
    }

    public void checkout(Payment payment) {

        payment.pay();
    }
}



/*
Anonymous Inner Class can also
extend abstract classes.
*/

abstract class Animal {

    abstract void sound();
}