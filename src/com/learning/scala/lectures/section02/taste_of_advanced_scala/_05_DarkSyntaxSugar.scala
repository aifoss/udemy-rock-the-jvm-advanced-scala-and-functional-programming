package com.learning.scala.lectures.section02.taste_of_advanced_scala

import scala.util.Try

/**
 * Created by sofia on 2019-09-29.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _05_DarkSyntaxSugar extends App {

    /* Syntax Sugar #1: Methods with Single Param */

    def singleArgMethod(arg: Int): String = s"$arg little ducks ...."

    val description = singleArgMethod {
        // write some complex code
        42
    }

    val aTryInstance = Try {
        throw new RuntimeException
    }

    List(1,2,3).map { x =>
        x + 1
    }


    /* Syntax Sugar #2: Single Abstract Method */

    trait Action {
        def act(x: Int): Int
    }

    val anInstance: Action = new Action {
        override def act(x: Int): Int = x + 1
    }

    val aFunkyInstance: Action = (x: Int) => x + 1

    // example: Runnable
    val aThread = new Thread(new Runnable {
        override def run(): Unit = println("Hello, Scala")
    })

    val aSweeterThread = new Thread(() => println("Sweet, Scala!"))

    abstract class AnAbstractType {
        def implemented: Int = 23
        def f(a: Int): Unit
    }

    val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")


    /* Syntax Sugar #3: The :: and #:: Methods */

    val prependedList = 2 :: List(3,4)
    // List(3,4).::(2)
    // last char decides associativity of method

    1 :: 2 :: 3 :: List(4,5)
    // List(4,5).::(3).::(2).::(1)

    class MyStream[T] {
        def -->:(value: T): MyStream[T] = this
    }

    val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]


    /* Syntax Sugar #4: Multi-Word Method Naming */

    class TeenGirl(name: String) {
        def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
    }

    val lilly = new TeenGirl("Lilly")
    lilly `and then said` "Scala is so sweet!"


    /* Syntax Sugar #5: Infix Types */

    class Composite[A, B]
    val composite: Int Composite String = ???

    class -->[A, B]
    val towards: Int --> String = ???


    /* Syntax Sugar #6: update() */

    val anArray = Array(1,2,3)
    anArray(2) = 7 // rewritten to anArray.update(2, 7)
    // used in mutable collections
    // remember apply() and update()


    /* Syntax Sugar #7: Setters for Mutable Containers */

    class Mutable {
        private var internalMember: Int = 0 // private for OO encapsulation

        def member: Int = internalMember // getter
        def member_=(value: Int): Unit =
            internalMember = value // setter
    }

    val aMutableContainer = new Mutable
    aMutableContainer.member = 42 // rewritten as aMutableContainer.member_=(42)

}
