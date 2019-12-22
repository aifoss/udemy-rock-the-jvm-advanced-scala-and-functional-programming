package com.learning.scala.lectures.section02.taste_of_advanced_scala

import scala.annotation.tailrec

/**
 * Created by sofia on 2019-09-29.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _04_Recap_TheScalaBasics extends App {

    val aCondition: Boolean = false
    val aConditionedVal = if (aCondition) 42 else 65

    // instructions vs expressions
    val aCodeBlock = {
        if (aCondition) 54
        56
    }

    // Unit
    val theUnit = println("Hello, Scala")

    // functions
    def aFunction(x: Int): Int = x + 1

    // recursion
    @tailrec
    def factorial(n: Int, accumulator: Int): Int = {
        if (n <= 0) accumulator
        else factorial(n-1, n*accumulator)
    }

    // object-oriented programming
    class Animal
    class Dog extends Animal
    val aDog: Animal = new Dog

    trait Carnivore {
        def eat(a: Animal): Unit
    }

    class Crocodile extends Animal with Carnivore {
        override def eat(a: Animal): Unit = println("crunch!")
    }

    // method notations
    val aCroc = new Crocodile
    aCroc.eat(aDog)
    aCroc eat aDog

    1 + 2
    1.+(2)

    // anonymous classes
    val aCarnivore = new Carnivore {
        override def eat(a: Animal): Unit = println("roar!")
    }

    // generics
    abstract class MyList[+A]

    // singletons and companions
    object myList

    // case classes
    case class Person(name: String, age: Int)

    // exceptions
    val throwsException = throw new RuntimeException // Nothing
    val aPotentialFailure = try {
        throw new RuntimeException
    } catch {
        case e: Exception => "I caught an exception"
    } finally {
        println("some logs")
    }

    // packaging and imports

    // functional programming
    val incrementer = new Function1[Int, Int] {
        override def apply(v1: Int): Int = v1 + 1
    }

    incrementer(1)

    val anonymousIncrementer = (x: Int) => x + 1

    List(1,2,3).map(anonymousIncrementer)

    // map, flatMap, filter

    // for-comprehension
    val pairs = for {
        num <- List(1,2,3)
        char <- List('a','b','c')
    } yield num+"-"+char

    // collections
    val aMap = Map(
        "John" -> 789,
        "Tom" -> 555
    )

    // Option, Try
    val anOption = Some(2)

    // pattern matching
    val x = 2
    val order = x match {
        case 1 => "first"
        case 2 => "second"
        case 3 => "third"
        case _ => x+"th"
    }

    val bob = Person("Bob", 22)
    val greeting = bob match {
        case Person(n, _) => s"Hi, my name is $n"
    }

    // all the patterns

}
