package com.learning.scala.lectures.section03.advanced_functional_programming

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _17_InfiniteStreamProficiency_MoreExercises extends App {

    /* Exercise */

    // 1. stream of Fibonacci numbers

    def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] = {
        new Cons(first, fibonacci(second, first + second))
    }

    println(fibonacci(1, 1).take(100).toList())

    // 2. stream of prime numbers with Eratosthenes' sieve

    def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
        if (numbers.isEmpty) numbers
        else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))

    println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())

}
