package com.learning.scala.lectures.section02.taste_of_advanced_scala

/**
 * Created by sofia on 2019-09-29.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _07_AdvancedPatternMatchingPart2 extends App {

    // infix patterns

    case class Or[A, B](a: A, b: B)

    val either = Or(2, "two")
    val humanDescription = either match {
//        case Or(number, string) => s"$number is written as $string"
        case number Or string => s"$number is written as $string"
    }

    println(humanDescription)


    // decomposing sequences

    val numbers = List(1)
    val vararg = numbers match {
        case List(1, _*) => "starting with 1" // will not work
    }

    abstract class MyList[+A] {
        def head: A = ???
        def tail: MyList[A] = ???
    }
    case object Empty extends MyList[Nothing]
    case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

    object MyList {
        def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
            if (list == Empty) Some(Seq.empty)
            else unapplySeq(list.tail).map(list.head +: _)
    }

    val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
    val decomposed = myList match {
        case MyList(1, 2, _*) => "starting with 1, 2"
        case _ => "something else"
    }

    println(decomposed)


    // custom return types for unapply()

    abstract class Wrapper[T] {
        def isEmpty: Boolean
        def get: T
    }

    class Person(val name: String, val age: Int)

    object PersonWrapper {
        def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
            def isEmpty = false
            def get: String = person.name
        }
    }

    val bob = new Person("Bob", 25)

    println(bob match {
        case PersonWrapper(n) => s"This person's name is $n"
        case _ => "An alien"
    })

}
