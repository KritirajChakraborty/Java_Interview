public class Annotations {

    public static void main(String[] args) {

        /*
        ANNOTATIONS INTERVIEW NOTES

        Most Important Annotations:

        1 @Override
            -> Method overriding validation

        2 @Deprecated
            -> Marks old API

        3 @SuppressWarnings
            -> Suppresses compiler warnings

        4 @FunctionalInterface
            -> Ensures exactly one abstract method

        5 @SafeVarargs
            -> Suppresses varargs generic warnings

        6 Custom Annotations
            -> User-defined metadata

        Interview Order:
        Override -> Deprecated -> SuppressWarnings
        -> FunctionalInterface -> Meta Annotations
        -> Custom Annotations
        */

        System.out.println("Annotations Interview Prep");
    }
}

/*
=========================================================
1 @Override
=========================================================

Purpose:
Tells compiler that a method is intended to override
a superclass method.

Benefits:
1 Compile-time checking
2 Prevents spelling mistakes
3 Improves readability

Example:

class Animal {
    void sound() {}
}

class Dog extends Animal {

    @Override
    void sound() {}
}
*/

/*
=========================================================
2 @Deprecated
=========================================================

Purpose:
Marks a class, method, or field as obsolete.

Benefits:
1 Warns developers
2 Guides migration to newer APIs

Example:

@Deprecated
void oldMethod() {}
*/

/*
=========================================================
3 @SuppressWarnings
=========================================================

Purpose:
Suppresses specific compiler warnings.

Common Values:
1 "unchecked"
2 "deprecation"
3 "rawtypes"

Example:

@SuppressWarnings("unchecked")
List list = new ArrayList();
*/

/*
=========================================================
4 @FunctionalInterface
=========================================================

Purpose:
Ensures interface contains exactly one
abstract method.

Used With:
Lambda Expressions

Example:

@FunctionalInterface
interface Payment {
    void pay();
}
*/

/*
=========================================================
5 @SafeVarargs
=========================================================

Purpose:
Suppresses heap pollution warnings for
generic varargs methods.

Example:

@SafeVarargs
static <T> void print(T... values) {}
*/

/*
=========================================================
6 META ANNOTATIONS
=========================================================

Meta annotations are annotations applied
to other annotations.

Important Ones:

1 @Target
2 @Retention
3 @Documented
4 @Inherited
5 @Repeatable
*/

/*
@Target

Defines where annotation can be used.

Examples:
ElementType.TYPE
ElementType.METHOD
ElementType.FIELD
ElementType.PARAMETER
*/

/*
@Retention

Defines lifecycle.

SOURCE
CLASS
RUNTIME

Most Interview Question:

Which retention policy is required for reflection?

Answer:
RUNTIME
*/

/*
=========================================================
7 CUSTOM ANNOTATION
=========================================================

Example:

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

@interface Audit {

    String value();
}
*/

/*
=========================================================
INTERVIEW QUESTIONS
=========================================================

Q1 Why use @Override?

Answer:
Compile-time validation of overridden methods.

---------------------------------------------------------

Q2 Difference between annotation and interface?

Answer:
Annotation provides metadata.
Interface provides behavior contract.

---------------------------------------------------------

Q3 Which retention policy is used with reflection?

Answer:
RetentionPolicy.RUNTIME

---------------------------------------------------------

Q4 Can annotations contain methods?

Answer:
Yes.
Annotation elements are declared like methods.

Example:

@interface Info {
    String author();
}

---------------------------------------------------------

Q5 Can we inherit annotations?

Answer:
Yes, using @Inherited
(for class-level annotations only).

---------------------------------------------------------

Q6 Which annotation is heavily used in Spring?

Answer:
@Component
@Service
@Repository
@Controller
@Autowired

---------------------------------------------------------

Q7 Can we create custom annotations?

Answer:
Yes.
Using @interface keyword.

---------------------------------------------------------

Q8 Difference between SOURCE, CLASS, and RUNTIME?

SOURCE:
Removed after compilation.

CLASS:
Stored in .class file.
Not available at runtime.

RUNTIME:
Available through reflection.
*/
