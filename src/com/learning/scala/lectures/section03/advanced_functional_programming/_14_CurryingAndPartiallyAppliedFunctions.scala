package com.learning.scala.lectures.section03.advanced_functional_programming

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _14_CurryingAndPartiallyAppliedFunctions extends App {

    // curried function
    val superAdder: Int => Int => Int = x => y => x + y

    val add3 = superAdder(3) // Int => Int = y => 3 + y

    println(superAdder(3)(5))
    println(add3(5))

    // curried method
    def curriedAdder(x: Int)(y: Int): Int = x + y

    // lifting = ETA-expansion
    // turning methods into functions
    val add4: Int => Int = curriedAdder(4)

    println(add4(5))

    // function ! method (JVM limitation)

    def inc(x: Int): Int = x + 1

    List(1,2,3).map(inc) // List(1,2,3).map(x => inc(x))

    // partially applied function (PAF)
    // turning methods into function values

    val add5 = curriedAdder(5) _ // Int => Int


    /* Exercise */

    val simpleAddFunction = (x: Int, y: Int) => x + y
    def simpleAddMethod(x: Int, y: Int): Int = x + y
    def curriedAddMethod(x: Int)(y: Int): Int = x + y

    val add7SAF = simpleAddFunction(7, _)
    val add7SAM = simpleAddMethod(7, _)
    val add7CAM = curriedAddMethod(7)(_)
    val add7CAM2 = curriedAddMethod(7) _

    println(add7SAF(3))
    println(add7SAM(3))
    println(add7CAM(3))
    println(add7CAM2(3))

    val add7 = (x: Int) => simpleAddFunction(7, x)
    val add7_2 = simpleAddFunction.curried(7)
    val add7_6 = simpleAddFunction(7, _: Int)
    val add7_3 = curriedAddMethod(7) _  // PAF
    val add7_4 = curriedAddMethod(7)(_) // PAF
    val add7_5 = simpleAddMethod(7, _: Int)


    // underscores are powerful
    // reducing function arity

    def concatenator(a: String, b: String, c: String): String = a + b + c

    val insertName = concatenator("Hello, I'm ", _: String, ", how are you?") // x: String => concatenator(hello, x, howareyou)
    println(insertName("John"))

    val fillInTheBlanks = concatenator("Hello, ", _:String, _:String) // (x, y) => concatenator("Hello, ", x, y)
    println(fillInTheBlanks("Tom, ", "Scala is awesome!"))


    /* Exercises */

    // 1. process a list of numbers and return their string representations with different formats
    //    use %4.2f, %8.6f, and %14.12f with a curried formatter function

    def curriedFormatter(s: String)(number: Double): String = s.format(number)

    val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

    val simpleFormat = curriedFormatter("%4.2f") _
    val seriousFormat = curriedFormatter("%8.6f") _
    val preciseFormat = curriedFormatter("%14.12f") _

    println(numbers.map(simpleFormat))
    println(numbers.map(seriousFormat))
    println(numbers.map(preciseFormat))
    println(numbers.map(curriedFormatter("%14.12f"))) // compiler does eta-expansion

    // 2. difference between:
    //    - functions vs methods
    //    - parameters: by-name vs 0-lambda

    def byName(n: => Int): Int = n + 1
    def byFunction(f: () => Int): Int = f() + 1

    def method: Int = 42
    def parenMethod(): Int = 42

    byName(23)
    byName(method)
    byName(parenMethod())
    byName(parenMethod) // ok but beware
//    byName(() => 42) // not ok
    byName((() => 42)())
//    byName(parenMethod _) // not ok

//    byFunction(45) // not ok
//    byFunction(method) // not ok
    byFunction(parenMethod) // compiler does eta-expansion
    byFunction(parenMethod _) // also works, but unnecessary
    byFunction(() => 46)

}
