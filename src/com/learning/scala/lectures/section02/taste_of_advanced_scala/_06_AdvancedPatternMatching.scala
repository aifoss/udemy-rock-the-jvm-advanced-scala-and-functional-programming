package com.learning.scala.lectures.section02.taste_of_advanced_scala

/**
 * Created by sofia on 2019-09-29.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _06_AdvancedPatternMatching extends App {

    val numbers = List(1)
    val description = numbers match {
        case head :: Nil => println(s"The only element is $head")
        case _ =>
    }


    // pattern matching for regular non-case class
    // create companion object with unapply() method

    class Person(val name: String, val age: Int)

    object PersonPattern {
        def unapply(person: Person): Option[(String, Int)] =
            if (person.age < 21) None
            else Some(person.name, person.age)

        def unapply(age: Int): Option[String] =
            Some(if (age < 21) "minor" else "major")
    }

    val bob = new Person("Bob", 25)
    val greeting = bob match {
        case PersonPattern(n, a) => s"Hi, my name is $n and my age is $a"
    }

    println(greeting)

    val legalStatus = bob.age match {
        case PersonPattern(status) => s"My legal status is $status"
    }


    /* Exercise */

    val n: Int = 45
    val mathProperty = n match {
        case x if x < 10 => "single digit"
        case x if x % 2 == 0 => "even number"
        case _ => "no property"
    }

    println(mathProperty)

    class Number(val n: Int)

    object NumberPattern {
        def unapply(n: Int): Option[String] =
            if (number.n < 10) Some("single digit")
            else if (number.n % 2 == 0) Some("even number")
            else Some("no property")
    }

    val number = new Number(10)
    val numberMatch = number.n match {
        case NumberPattern(result) => s"$result"
    }

    println(numberMatch)

    object even {
        def unapply(arg: Int): Option[Boolean] =
            if (arg % 2 == 0) Some(true)
            else None
    }

    object singleDigit {
        def unapply(arg: Int): Option[Boolean] =
            if (arg > -10 && arg < 10) Some(true)
            else None
    }

    val n2 = 5
    val n2Property = n2 match {
        case even(_) => "even number"
        case singleDigit(_) => "single digit"
        case _ => "no property"
    }

    println(n2Property)

    object evenSimpler {
        def unapply(arg: Int): Boolean = arg % 2 == 0
    }

    object singleDigitSimpler {
        def unapply(arg: Int): Boolean = arg > -10 && arg < 10
    }

    val n3 = 15
    val n3Property = n3 match {
        case evenSimpler() => "even number"
        case singleDigitSimpler() => "single digit"
        case _ => "no property"
    }

    println(n3Property)

}
