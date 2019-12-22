package com.learning.scala.lectures.section03.advanced_functional_programming

import scala.annotation.tailrec

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _16_LazyEvaluationExercise_PotentiallyInfiniteStream {

    /* Exercise */

    // implement a lazily-evaluated, singly-linked stream of events

    // naturals = MyStream.from(1)(x => x+1) = stream of natural numbers (potentially infinite)
    // naturals.take(100).foreach(println) // lazily-evaluated stream of first 100 naturals
    // naturals.foreach(println) // will crash
    // naturals.map(_ * 2)

}


abstract class MyStream[+A] {

    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    // prepend
    def #::[B >: A](elem: B): MyStream[B]
    // concatenate
    def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B]

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    // take first n elements from stream
    def take(n: Int): MyStream[A]
    def takeAsList(n: Int): List[A] = take(n).toList()

    /*
        [1 2 3].toList([]) =
        [2 3].toList([1]) =
        [3].toList([2 1]) =
        [].toList([3 2 1]) =
        [1 2 3]
     */
    @tailrec
    final def toList[B >: A](acc: List[B] = Nil): List[B] =
        if (isEmpty) acc.reverse
        else tail.toList(head :: acc)

}


object EmptyStream extends MyStream[Nothing] {

    def isEmpty: Boolean = true
    def head: Nothing = throw new NoSuchElementException
    def tail: MyStream[Nothing] = throw new NoSuchElementException

    // prepend
    def #::[B >: Nothing](elem: B): MyStream[B] = new Cons(elem, this)
    // concatenate
    def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

    def foreach(f: Nothing => Unit): Unit = ()
    def map[B](f: Nothing => B): MyStream[B] = this
    def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
    def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

    // take first n elements from stream
    def take(n: Int): MyStream[Nothing] = this

}


class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {

    def isEmpty: Boolean = false
    override val head: A = hd
    override lazy val tail: MyStream[A] = tl // call by need

    // prepend
    /*
        val s = new Cons(1, EmptyStream)
        val prepended = 1 #:: s = new Cons(1, s)
     */
    def #::[B >: A](elem: B): MyStream[B] = new Cons(elem, this)
    // concatenate
    def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

    def foreach(f: A => Unit): Unit = {
        f(head)
        tail.foreach(f)
    }

    /*
        s = new Cons(1, ?)
        mapped = s.map(_ + 1) = new Cons(2, s.tail.map(_ + 1))
     */
    def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))
    def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

    def filter(predicate: A => Boolean): MyStream[A] = {
        if (predicate(head)) new Cons(head, tail.filter(predicate))
        else tail.filter(predicate)
    }

    // take first n elements from stream
    def take(n: Int): MyStream[A] =
        if (n <= 0) EmptyStream
        else if (n == 1) new Cons(head, EmptyStream)
        else new Cons(head, tail.take(n-1))

}


object MyStream {

    def from[A](start: A)(generator: A => A): MyStream[A] =
        new Cons(start, MyStream.from(generator(start))(generator))

}


object StreamPlayground extends App {

    val naturals = MyStream.from(1)(_ + 1)

    println(naturals.head)
    println(naturals.tail.head)
    println(naturals.tail.tail.head)
    println

    val startFrom0 = 0 #:: naturals

    println(startFrom0.head)
    println

    startFrom0.take(10000).foreach(println)
    println

    println(startFrom0.map(_ * 2).take(100).toList())
    println

    println(startFrom0.flatMap(x => new Cons(x, new Cons(x+1, EmptyStream))).take(100).toList())
    println

    println(startFrom0.filter(_ < 10).take(10).take(20).toList())
    println

}
