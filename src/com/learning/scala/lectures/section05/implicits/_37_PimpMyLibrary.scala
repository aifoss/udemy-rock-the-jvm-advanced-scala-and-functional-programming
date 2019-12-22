package com.learning.scala.lectures.section05.implicits

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _37_PimpMyLibrary extends App {

    /*
        Type Enrichment
     */

    implicit class RichInt(val value: Int) extends AnyVal {
        def isEven: Boolean = value % 2 == 0
        def sqrt: Double = Math.sqrt(value)
    }

    implicit class RicherInt(richInt: RichInt) {
        def isOdd: Boolean = richInt.value % 2 != 0
    }

    new RichInt(42).sqrt

    2.isEven

    1 to 10

    import scala.concurrent.duration._
    3.seconds

//    42.isOdd


    /*
        Exercise:

        Enrich the string class:
        - asInt
        - encrypt
          "John" -> Lqjp

        Keep enriching the Int class:
        - times(function)
          3.times(() => ...)
        - *
          3 * List(1,2) => List(1,2,1,2,1,2)
     */

    implicit class RichString(val string: String) extends AnyVal {
        def asInt: Int = Integer.valueOf(string)
        def encrypt(by: Int): String = {
            string.map(c => (c+by).asInstanceOf[Char])
        }
    }

    println("3".asInt)
    println("John".encrypt(2))

    implicit class RichInt2(val value: Int) extends AnyVal {
        def times(function: () => Unit): Unit = {
            def timesAux(n: Int): Unit = {
                if (n <= 0) ()
                else {
                    function()
                    timesAux(n-1)
                }
            }

            timesAux(value)
        }

        def *[T](list: List[T]): List[T] = {
            def concatenate(n: Int): List[T] = {
                if (n <= 0) List()
                else concatenate(n-1) ++ list
            }

            concatenate(value)
        }
    }

    3.times(() => println("Scala rocks!"))

    println(4 * List(1,2))


    implicit def stringToInt(string: String): Int = Integer.valueOf(string)

    println("6" / 2)

    class RichAltInt(value: Int)
    implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

    // danger zone
    implicit def intToBoolean(i: Int): Boolean = i == 1
    val aConditionedValue = if (3) "OK" else "Something wrong"

    println(aConditionedValue)


    /*
        Tips:
        1. Keep type enrichment to implicit classes and type classes.
        2. Avoid implicit defs as much as possible.
        3. Package implicits clearly, bring into scope only what you need.
        4. If you need conversions, make them specific.
     */

}
