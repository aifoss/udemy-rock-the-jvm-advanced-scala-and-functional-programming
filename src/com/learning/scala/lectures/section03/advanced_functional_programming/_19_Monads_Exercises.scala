package com.learning.scala.lectures.section03.advanced_functional_programming

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _19_Monads_Exercises extends App {

    /* Exercises */

    // 1. implement a Lazy[T] monad
    //    unit/apply
    //    flatMap

    class Lazy[+A](value: => A) {

        private lazy val internalValue = value

        def use: A = internalValue

        def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)

    }

    object Lazy {
        def apply[A](value: => A): Lazy[A] = new Lazy(value)
    }

    val lazyInstance = Lazy {
        println("Today I don't feel like doing anything")
        42
    }

    val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
        10 * x
    })

    val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
        10 * x
    })

    flatMappedInstance.use
    flatMappedInstance2.use

    /*

        left identity:
        unit.flatMap(f) = f(v)
        Lazy(v).flatMap(f) = f(v)

        right identity:
        lazy.flatMap(unit) = lazy
        Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

        associativity
        lazy.flatMap(f).flatMap(g) = lazy.flatMap(x => f(x).flatMap(g))
        Lazy(v).flatMap(f).flagMap(g) = f(v).flatMap(g)
        Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)

     */


    // 2. implement map and flatten, given Monad with flatMap

    /*

        Monda[T] { // List
            def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)

            def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]

            def flatten(m : Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)

        List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
        List(List(1,2), List(3,4)).flatten = List(List(1,2), List(3,4)).flatMap(x => x) = List(1,2,3,4)

     */

}
