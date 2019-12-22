package com.learning.scala.lectures.section03.advanced_functional_programming

import scala.annotation.tailrec

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object FunctionalSet {

}

trait MySet[A] extends (A => Boolean) {

    def apply(elem: A): Boolean = {
        contains(elem)
    }

    def contains(elem: A): Boolean
    def +(elem: A): MySet[A]
    def ++(anotherSet: MySet[A]): MySet[A]

    def map[B](f: A => B): MySet[B]
    def flatMap[B](f: A => MySet[B]): MySet[B]
    def filter(predicate: A => Boolean): MySet[A]

    def foreach(f: A => Unit): Unit

}

class EmptySet[A] extends MySet[A] {

    override def contains(elem: A): Boolean = false

    override def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)

    override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

    override def map[B](f: A => B): MySet[B] = new EmptySet[B]

    override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

    override def filter(predicate: A => Boolean): MySet[A] = this

    override def foreach(f: A => Unit): Unit = ()

}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

    override def contains(elem: A): Boolean =
        elem == head || tail.contains(elem)

    override def +(elem: A): MySet[A] =
        if (this contains elem) this
        else new NonEmptySet[A](elem, this)

    override def ++(anotherSet: MySet[A]): MySet[A] =
        tail ++ anotherSet + head

    override def map[B](f: A => B): MySet[B] =
        (tail map f) + f(head)

    override def flatMap[B](f: A => MySet[B]): MySet[B] =
        (tail flatMap f) ++ f(head)

    override def filter(predicate: A => Boolean): MySet[A] = {
        val filteredTail = tail filter predicate
        if (predicate(head)) filteredTail + head
        else filteredTail
    }

    override def foreach(f: A => Unit): Unit = {
        f(head)
        tail foreach f
    }

}


object MySet {

    def apply[A](values: A*): MySet[A] = {
        @tailrec
        def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] = {
            if (valSeq.isEmpty) acc
            else buildSet(valSeq.tail, acc+ valSeq.head)
        }

        buildSet(values.toSeq, new EmptySet[A])
    }

}


object FunctionalSetPlayground extends App {

    val s: MySet[Int] = MySet(1,2,3,4)

    s + 5 ++ MySet(-1,-2) + 3 map(x => x * 10) foreach println
    println

    s flatMap(x => MySet(x, x * 10)) filter(_ % 2 == 0) foreach println
    println

}