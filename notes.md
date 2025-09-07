# Scala Notes

This is a summary of the instructions for [Jetbrains Academy's Scala tutorial](https://github.com/jetbrains-academy/scala-tutorial).
I concentrated on the points I either did not know or forgot from when I took functional programming in SML.
I also concentrated on the differences between Scala and SML.
Some of the notes are copied ad verbatim from the Jetbrains tutorial. I tried my best to blockquote but I cannot guarantee adhering to scientific citing standards.
Best to assume this is all Jetbrains Academy's work.
I also did screw up the repository, so even Jetbrains' initial commits show up as mine. THIS IS FALSE INFO.
I only contributed the solutions (except where I mentioned that I peeked the solution)!

## Operators Are Methods

```Scala
1 + 2 == 1.+(2)

1.to(10) == 1 to 10
```

## Val vs. Def

RHS of def evaluated on each use.

RHS of val evaluated ONCE at definition itself.

```Scala
def x = 2
val y = square(x)
```

Y here means 4, and always 4.

Non-terminating RHS are problematic when using val:

```Scala
def loop: Int = loop
def x = loop    // ok
val x = loop    // does not terminate
```

## Evaluation

### Substitution model

Applications of parametrized functions are evaluated like this:
1. Evaluate all arguments from left to right
2. Replace function application by RHS of function 
3. Replace parameters by actual arguments

### Example (call-by-value)

```Scala
sumOfSquares(3, 2+2)
sumOfSquares(3, 4)
square(3) + square(4)
3 * 3 + square(4)
9 + square(4)
9 + 4 * 4
9 + 16
25
```

The substitution model reduces and expression to a value.
Can be applied to all expressions as long as expression has NO SIDE EFFECTS.
(See lambda calculus for further info.)

## Call-by-value and call-by-name

The evaluation strategy in the example reduces the arguments before substituing the RHS of the function. This is called call-by-value.

We could also apply the RHS to the unreduced arguments.
This is called call-by-name.

### Example (call-by-name)

```Scala
sumOfSquares(3, 2+2)
square(3) + square(2+2)
3 * 3 + square(2+2)
9 + square(2+2)
9 + (2+2) * (2+2)
9 + 4 * (2+2)
9 + 4 * 4
9 + 16
25
```

Both are semantically equivalent as long as 
- the reduced expression consists of pure functions (WTF?)
- both evaluations terminate

Call-by-value evals every argument ONCE.

Call-by-name ONLY evals arguments USED in the function body.

## Semicolons

Optional at end of line, except for more than one statement per line:

```Scala
val y = x + 1; y * y
```

This can be a problem when spanning one expression across multiple lines:

```Scala
someLongExpression
+ someOtherExpression
```

equals

```Scala
someLongExpression;
+ someOtherExpression
``` 
   
which can be overcome by both of these semantically equivalent variants:

```Scala
(someLongExpression
  + someOtherExpression)
```

and

```Scala
someLongExpression + 
  someOtherExpression
```

## Top-Level Definitions

`def` and `val` must be used inside top-level OBJECT DEFINITIONS.

```Scala
object MyExecutableProgram {
  val myVal = ...
  def myMethod = ...
}
```

The definition of the object MyExecutableProgram is top-level because it is not nested inside another definition.

## Packages and Imports

Multiple files can be organized into packages.
Just write

```Scala
package bla
```

at the start of a file.
Definitions inside the same package are visible in all definitions in all files of the same package.
Imports work analogous to Java.

## Writing Executable Programs

Provide a main method like so:

```Scala
object Hello {
  def main(args: Array[String]) = println("Hello, world!")
}
```

## Case Class

Equivalent to structs.

```Scala
case class Note(
                 name: String, 
                 duration: String, 
                 octave: Int
    )
```

## Algebraic Data Types

### Sealed Traits

Fixed set of alternatives.

```Scala
sealed trait Symbol

case class Note(
                 name: String, 
                 duration: String, 
                 octave: Int) extends Symbol

case class Rest(duration: String) extends Symbol
```

Here, the sealed trait was expressed using case classes,
because we want to aggregate information inside of the alternative classes.
When we do not need to aggregate additional information, 
but just distinguish between alternatives of a sealed trait, we use case objects:
```Scala
sealed trait NoteName
case object A extends NoteName
case object B extends NoteName
...
case object G extends NoteName
```


### Pattern Matching

Symbol does not have any members. We cannot do anything with a "mere" Symbol.
We have to distinguish between the different cases. We can use pattern matching:

```Scala
    symbol match {
        case Note(name, duration, octave) => duration
        case Rest(duration) => duration
    }
```

`case Rest(duration) => ...` is a CONSTRUCTOR PATTERN. It matches vals of type Rest that have been constructed with arguments matching the pattern `duration`.
`duration` is a VARIABLE PATTERN. It matches ANY value and binds it to the name `duration`.

The WHOLE match expression is rewritten to RHS of the first case where the pattern matches.
References to pattern vars are replaced by corresponding parts in the selector. (WTF? What are "references to pattern vars"?)



## Tail Recursion

Iff a function calls itself as its last action, we can reuse the stack frame.
O(1) in stack space. ==> Just iteration.
This is the functional equivalent of a loop, and just as efficient.

Add `@tailrec` as an annotation to require a function to be tail recursive.

## Anonymous Functions

Example of an anonymous function:

```Scala
(x: Int) => x * x * x
```

This is only syntactic sugar for

```Scala
{ def f(x1: T1, ..., xn: TN) = e; f }
```

where f has not been bound to anything.

## Lists

- Immutable
- Recursive
- Homogeneous

Lists pattern match as expected.

### Construction of Lists

```Scala
val abc = "A" :: ("B" :: ( "C" :: Nil))
val empty = Nil
```

Convention: Operators ending in `:` are right-associative.
(This means we can omit the Parentheses in the definition of `abc`.)

Operators ending in `:` are METHOD CALLS on RHS operand(!).

```Scala
val abc = Nil.::("C").::("B").::("A")
```

### Common Operations on Lists

```Scala
abc.map(x => x.toLowerCase) == List("a", "b", "c")
abc.filter(x => x!= "A") == List("B", "C")
abc.flatMap{
  x => List(x,x.toLowerCase)
} == List("A","a","B","b","C","c")
```

## Optionals

```Scala
def sqrt(x: Double): Option[Double] =
  if (x < 0) None else Some(...)

def foo(x: Double): String =
  sqrt(x) match {
    case None => "no result"
    case Some(y) => y.toString
  }
```

## Error Handling

### Example:

```Scala
def tryit(x: Bool): Try[Bool] =
  if (x == true) Success(x)
  else Failure(new IllegalArgumentException("x was not true"))
```

This example raises an error iff `tryit` is provided with `false`.
`Try[A]` is an algebraic data type, so we can use pattern matching on it.
`Try[A]` also has `map`, `filter` and `flatMap`.
They behave the same as with Optionals, but any exception thrown is converted into `Failure`.

### Either

`Either[A, B]` has either Type A or Type B.
Decomposes into `Left` and `Right`.
Difference to `Try` is that the exception does not HAVE to be `Throwable.`
Also, exceptions occuring are not converted into failures.

Does have `map` and `flatMap`. ONLY ON `Right` case!

## Syntactic Conveniences

### String Interpolation

We want to put values into constant strings at runtime.
Put an `s` in front of the string.
Then you can put dynamic values into it using `$val`, or `${expression}` for complex expressions.

Example:

```Scala
s"Hello, ${"World".toUpperCase}!"
```

### Tuples

Analogous to `SML`.
First element with `var._1`, second element with `var._2`.

### Functions

Functions are objects in Scala:

`A => B` is just syntactic sugar for `scala.Function[A, B]`:

```Scala
package scala
trait Function1[A, B] {
  def apply(x: A): B 
```

Functions are OBJECTS with APPLY methods.

Anonymous functions like `(x: Int) => x * x` are expanded to:

```Scala
class AnonFun extends Function1[Int, Int] {
  def apply(x: Int) = x * x
}
new AnonFun
```

Using anonymous class syntax:

```Scala
new Function1[Int, Int] {
  def apply(x: Int) = x * x
}
```

```Scala
f(a, b)
```

is equivalent to

```Scala
f.apply(a,b)
```

### Functions vs. Methods

Methods

```Scala
def f(x: Int): Boolean = true
```

are NOT function values.

If `f` is used in a place where a function type is expected,
it is converted to the function value

```Scala
(x: Int) => f(x)
```

## For expressions

```Scala
xs.map(x => x + 1)
```

is equivalent to

```Scala
for (x <- xs) yield x + 1
```

```Scala
xs.filter(x => x % 2 == 0)
```

is equivalent to

```Scala
for (x <- xs if x % 2 == 0 <- xs) yield x
```

```Scala
xs.flatMap(x => ys.map(y => (x, y)))
```

is equivalent to

```Scala
for (x <- xs; y <- ys) yield (x, y)
```

## Method Parameters

### Named Parameters

```Scala
f(start: Int, end: Int, step: Int)
```

Function call then is

```Scala
f(start = 1, end = 10, step = 1)
```

### Default values

```Scala
f(start: Int = 0, end: Int = 9, step: Int = 1)
```

Remember that this is the function DEFINITION.

### Repeated Parameters

```Scala
f(neutralElement: Int, valuesToAdd: Int*): Int = ...
```

Adds all values in valuesToAdd to the neutral element.

## Object Oriented Programming

- Public by default, private using `private`

### Preconditions

We can express preconditions using the `require()` function:

```Scala
  def division(nominator: Int, denominator: Int): Int = 
    require(denominator != 0, "cannot divide by zero")
```
    
`require` throws an `IllegalArgumentException` with the message string on violation of the precondition.

### Assertions

`assert()` throws an `AssertionError` on violation, the syntax is the same as for `require`.

- `require` enforces preconditions on the caller of a function 
- `assert` checks the code of the function itself

### Constructors

`Primary constructor` is the same as everywhere.
`Auxiliary constructors` are built using `this`.

```Scala
class blabla(x:INT, b:Bool) {
  def this() = this(0, true)
}
```


### Classes and Substitutions

We have seen how function applications are substituted. How does this work for classes?

```Scala
new C(e0, ..., en)
```

The arguments are evaluated like in a function application.
The resulting expression is a value.

Consider this definition:

```Scala
class C(x0, ..., xn) {
...
def f(y0, ..., ym) = b
...
}
```

How is this

```Scala
new C(v0, ..., vn).f(w0, ..., wm)
```

expression evaluated?

THREE substitutions:
- `y0... ym` is substituted by `w0... wm` (function parameters substituted by arguments)
- `x0 ... xn` is substituted by `v0... vm` (class parameters substituted by class arguments)
- self reference `this` is substituted by value of object `new C(...)`

### Operators

Identifiers can be
- alphanumeric
- symbolic (operators)

So we can define methods to have symbolic names:

```Scala
class Number() {
  def + (n: Number, m:Number) = ...
...
}
```

## Abstract Classes

```Scala
abstract class Base {
def foo = 1
def bar: Int
}

class Sub extends Base {
override def foo = 2
def bar = 3
}
```

## Singleton Objects

```Scala
object Single extends Base {
def bar = math.Pi
}
```

This defines ONE and ONE instance of Single alone.

## Traits

Traits allow subclasses to inherit from "multiple superclasses".
(Remember that you can only inherit from ONE class in Scala!)

Traits are declared like abstract classes:

```Scala
trait Planar {
def height: Int
def width: Int
def surface: height * width
}
```

```Scala
class Square extends Shape with Planar with Movable
...
```

Traits CANNOT have parameters.

## Imperative Programming

Mutable state is expressed using VARIABLES.
Variables are defined like values, but using the keyword `var`.

```Scala
var x = 0

x = x + 1
```

Objects with state have some variable members.



### Operational Equivalence

- Suppose we have two definitions `x` and `y`
- `x` and `y` are operationally equivalent iff no possible test can distinguish between them

We can check for operational equivalence like so:

- Execute the definitions `x` and `y` followed by a sequence `s` of operations on them

```Scala
val x = 0
val y = 1
f(x,y)
```


- Execute the definitions followed by a sequence `s'` where we rename all occurences of `y` with `x`

```Scala
val x = 0
val y = 1
f(x,x)
```

- If the results are different, `x` and `y` are different
- If ALL POSSIBLE `(s,s')` produce the same result, `x` and `y` are the same

## Imperative Loops

### While

```Scala
var i = 10
while (i > 0) { /* do something*/; i = i - 1 }
```

### For

```Scala
for (i <- 1 until 3) { System.out.print(i) + " " }
```

prints `1 2`

## Classes vs. Case Classes

- Classes require `new`, Case Classes do not
- Case Class parameters are saved as MEMBERS (so they are PUBLIC)
- Case Classes are IMMUTABLE

### Equality

- In Scala, equality is identity by default, however for case classes equality is value comparison on the aggregated information.

### Pattern matching

- Pattern matching only works on Case Classes (by default)

## Polymorphic Types

### Type Parameters

```Scala
abstract class Set[A] {
def incl(a: A): Set[A]
def contains(a: A): Boolean
}
```

Type parameters are written in square brackets.

### Generic Functions

Functions can have type parameters as well.

```Scala
def f[A](para: A): Boolean = true
```

### Type Inference

Scala does infer missing types.

### Types and Evaluation

Scala deletes types before evaluating. This is called "type erasure" and done in other languages like Java, Haskell, ML, OCaml.

### Polymorphism

Two forms: Subtyping, Generics.


## Type Bounds

```Scala
A <: Animal
```

This means that A can only be instantiated with a subtype of Animal.

```Scala
A :> Animal
```

This means that A can only be instantiated with a supertype of Animal.

## Covariance

```Scala
trait Field[A] {
def get: A
}
```
Given `Zebra <: Mammal`, it holds that `Field[Zebra] <: Field[Mammal]`.
We say `Field` is `covariant`.
Covariance does not hold for Arrays:

    Zebra[] zebras = new Zebra[]{ new Zebra() }  // Array containing 1 `Zebra`
    Mammal[] mammals = zebras      // Allowed because arrays are covariant in Java
    mammals[0] = new Giraffe()     // Allowed because a `Giraffe` is a subtype of `Mammal`
    Zebra zebra = zebras[0]        // Get the first `Zebra` … which is actually a `Giraffe`!

(This was Java code, btw.)
    
### The Liskov Substitution Principle

If `A <: B`, then everything you can do with a value of type B, you should be able to do with a value of type A.

### Variance

A type that can mutate its elements should NOT be covariant.
Immutable types can be covariant if certain conditions on methods are met.

### Def (Variance)

Let `C[T]` be a parameterized type and `A` and `B` types s.t. `A <: B`.
Then there are three possible relationships between `C[A]` and `C[B]`:
- `C[A] <: C[B]`, C is covariant
- `C[A] :> C[B]`, C is contravariant
- Neither `C[A]` nor ` C[B]` are subtypes of each other, C is nonvariant

Scala lets you annotate the type parameter:

```Scala
class C[+A] {...} // C is covariant

class C[-A] {...} // C is contravariant

class C[A] {...}  // C is nonvariant
```

### Typing rules for functions

    If A2 <: A1 and B1 <: B2, then

    A1 => B1 <: A2 => B2

    So functions are contravariant in their argument type(s) and covariant in their result type.

### Variance Checks

    covariant type parameters can only appear in method results.
    contravariant type parameters can only appear in method parameters.
    invariant type parameters can appear anywhere.
    
    BUT:
    covariant type parameters may appear in the lower bounds of method type parameters;
    contravariant type parameters may appear in the upper bounds of a method.

### Example(Function Trait)

    ```Scala
    trait Function1[-T, +U] {
        def apply(x: T): U
      }
    ```

### Lower bounds

Suppose we have a Stream of elements:

    trait Stream[+T] {
      def prepend(elem: T): Stream[T] = Stream.cons(elem, this)
    }
    
Suppose those elements are mammals and we want to prepend a Giraffe:

    ```Scala
    mammals = Stream[Mammal]
    mammals.prepend(new Giraffe)
    ```

That checks out.
However, if we wanted to prepend this:

    ```Scala
    zebras = Stream[Zebra]
    zebras.prepend(new Giraffe)
    ```

We get a type error, because `Stream[Zebra]` cannot be a subtype of `Stream[Mammal]`.

We have to use a LOWER BOUND:
    ```Scala
    def prepend [U >: T](elem: U): Stream[U] = 
          Stream.cons(elem, this)
    ```
    
Remember: 
    
    covariant type parameters may appear in the lower bounds of method type parameters;

    
## Lazy Lists

Lists can be lazily evaluated. That means that we only construct the tail when it is accessed/referenced/needed.

    ```Scala
    val xs = LazyList.cons(1, LazyList.empty)
    ```

    ```Scala
    llRange(lo: Int, hi: Int): LazyList[Int]
    ```

    ```Scala
    x #:: xs = LazyList.cons(x, xs)
    ```

## Lazy Evaluation

- lazy evaluation: tail evaluation result is memoized and loaded at subsequent eval of tail
- by-name-evaluation: everything is recomputed
- strict evaluation: parameters and val definitions

Scala uses strict evaluation by default. It allows lazy evaluation of val definitions using `lazy val`.

    Scala```
    lazy val x = expr
    ```
    
## Type Classes

### Parameterized sort

Is analogous to SML. We can use a comparison function to parameterize sorting functions and make them "Polymorphic".

Scala does also have orderings:

    ```Scala
    scala.math.Ordering[T]
    ```

We can parameterize sorting functions with orderings.

    ```Scala
    insertionSort(nums)(Ordering.Int)
    ```

### Implicit Parameters

    ```Scala
    def insertionSort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {...}
    ```
    
Then, the Scala compiler infers the correct `ord`.

### Rules

    - is marked implicit;
    - has a type compatible with T;
    - is visible at the point of the function call or is defined in a companion object associated with T.

### Def(Type Classes)

types parameterized and implicit parameters == type classes
