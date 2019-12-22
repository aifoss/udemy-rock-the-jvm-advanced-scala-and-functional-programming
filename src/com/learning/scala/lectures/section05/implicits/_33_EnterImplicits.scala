package com.learning.scala.lectures.section05.implicits

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _33_EnterImplicits extends App {

    val pair: (String, String) = "John" -> "555"
    val intPair: (Int, Int) = 1 -> 2

    case class Person(name: String) {
        def greet = s"Hi, my names is $name!"
    }

    implicit def fromStringToPerson(str: String): Person = Person(str)

    println("Peter".greet)

    def increment(x: Int)(implicit amount: Int): Int = x + amount

    implicit val defaultAmount = 10

    println(increment(2))

}
