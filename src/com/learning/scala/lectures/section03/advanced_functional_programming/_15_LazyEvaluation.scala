package com.learning.scala.lectures.section03.advanced_functional_programming

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _15_LazyEvaluation extends App {

    // lazy delays evaluation of values
    // once evaluated, value remains
    lazy val x: Int = {
        println("hello")
        42
    }

    println(x)
    println(x)
    println


    // implications

    // side effects

    def sideEffectCondition: Boolean = {
        println("boo")
        true
    }

    def simpleCondition: Boolean = false

    lazy val lazyCondition = sideEffectCondition

    println(if (simpleCondition && lazyCondition) "yes" else "no")
    println


    // in conjunction with call by name
    // call by need

    def byNameMethod(n: => Int): Int = {
        lazy val t = n
        t + t + t + 1
    }

    def retrieveMagicValue: Int = {
        // side effect of a long computation
        println("waiting")
        Thread.sleep(1000)
        42
    }

    println(byNameMethod(retrieveMagicValue))
    println


    // filtering with lazy vals

    def lessThan30(i: Int): Boolean = {
        println(s"$i is less than 30?")
        i < 30
    }

    def greaterThan20(i: Int): Boolean = {
        println(s"$i is greater than 20?")
        i > 20
    }

    val numbers = List(1, 25, 40, 5, 23)
    val lt30 = numbers.filter(lessThan30)
    val gt20 = lt30.filter(greaterThan20)

    println(gt20)
    println

    val lt30Lazy = numbers.withFilter(lessThan30)
    val gt20Lazy = lt30Lazy.withFilter(greaterThan20)

    println(gt20Lazy)
    println

    gt20Lazy.foreach(println)


    // for-comprehensions use withFilter with guards

    for {
        a <- List(1,2,3) if a % 2 == 0 // lazy vals
    } yield a + 1

    List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1)

}
