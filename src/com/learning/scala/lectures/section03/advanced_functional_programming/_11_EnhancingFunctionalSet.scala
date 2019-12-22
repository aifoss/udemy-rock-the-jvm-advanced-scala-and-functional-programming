package com.learning.scala.lectures.section03.advanced_functional_programming

import scala.annotation.tailrec

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object EnhancingFunctionalSet {

}


trait MyESet[A] extends (A => Boolean) {

    def apply(elem: A): Boolean = {
        contains(elem)
    }

    def contains(elem: A): Boolean
    // add
    def +(elem: A): MyESet[A]
    // union
    def ++(anotherSet: MyESet[A]): MyESet[A]

    def map[B](f: A => B): MyESet[B]
    def flatMap[B](f: A => MyESet[B]): MyESet[B]
    def filter(predicate: A => Boolean): MyESet[A]

    def foreach(f: A => Unit): Unit

    // remove
    def -(elem: A): MyESet[A]
    // difference
    def --(anotherSet: MyESet[A]): MyESet[A]
    // intersection
    def &(anotherSet: MyESet[A]): MyESet[A]

}


class EmptyESet[A] extends MyESet[A] {

    override def contains(elem: A): Boolean = false

    override def +(elem: A): MyESet[A] = new NonEmptyESet[A](elem, this)

    override def ++(anotherSet: MyESet[A]): MyESet[A] = anotherSet

    override def map[B](f: A => B): MyESet[B] = new EmptyESet[B]

    override def flatMap[B](f: A => MyESet[B]): MyESet[B] = new EmptyESet[B]

    override def filter(predicate: A => Boolean): MyESet[A] = this

    override def foreach(f: A => Unit): Unit = ()

    override def -(elem: A): MyESet[A] = this

    override def --(anotherSet: MyESet[A]): MyESet[A] = this

    override def &(anotherSet: MyESet[A]): MyESet[A] = this

}

class NonEmptyESet[A](head: A, tail: MyESet[A]) extends MyESet[A] {

    override def contains(elem: A): Boolean =
        elem == head || tail.contains(elem)

    override def +(elem: A): MyESet[A] =
        if (this contains elem) this
        else new NonEmptyESet[A](elem, this)

    override def ++(anotherSet: MyESet[A]): MyESet[A] =
        tail ++ anotherSet + head

    override def map[B](f: A => B): MyESet[B] =
        (tail map f) + f(head)

    override def flatMap[B](f: A => MyESet[B]): MyESet[B] =
        (tail flatMap f) ++ f(head)

    override def filter(predicate: A => Boolean): MyESet[A] = {
        val filteredTail = tail filter predicate
        if (predicate(head)) filteredTail + head
        else filteredTail
    }

    override def foreach(f: A => Unit): Unit = {
        f(head)
        tail foreach f
    }

    override def -(elem: A): MyESet[A] =
        if (head == elem) tail
        else (tail - elem) + head

    override def --(anotherSet: MyESet[A]): MyESet[A] =
        filter(x => !anotherSet(x))

    override def &(anotherSet: MyESet[A]): MyESet[A] =
//        filter(x => anotherSet.contains(x))
//        filter(x => anotherSet(x))
        filter(anotherSet)

}


object MyESet {

    def apply[A](values: A*): MyESet[A] = {
        @tailrec
        def buildSet(valSeq: Seq[A], acc: MyESet[A]): MyESet[A] = {
            if (valSeq.isEmpty) acc
            else buildSet(valSeq.tail, acc+ valSeq.head)
        }

        buildSet(values.toSeq, new EmptyESet[A])
    }

}


object EnhancedSetPlayground extends App {

    val s1: MyESet[Int] = MyESet(1,2,3,4)
    val s2: MyESet[Int] = MyESet(3,4,5,6)

    s1 - 4 foreach println
    println

    s1 -- s2 foreach println
    println

    s1 & s2 foreach println
    println

}
