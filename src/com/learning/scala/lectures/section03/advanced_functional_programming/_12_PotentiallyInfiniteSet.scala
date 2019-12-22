package com.learning.scala.lectures.section03.advanced_functional_programming

import scala.annotation.tailrec

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object PotentiallyInfiniteSet {

}


trait MyPSet[A] extends (A => Boolean) {

    def apply(elem: A): Boolean = {
        contains(elem)
    }

    def contains(elem: A): Boolean
    // add
    def +(elem: A): MyPSet[A]
    // union
    def ++(anotherSet: MyPSet[A]): MyPSet[A]

    def map[B](f: A => B): MyPSet[B]
    def flatMap[B](f: A => MyPSet[B]): MyPSet[B]
    def filter(predicate: A => Boolean): MyPSet[A]

    def foreach(f: A => Unit): Unit

    // remove
    def -(elem: A): MyPSet[A]
    // difference
    def --(anotherSet: MyPSet[A]): MyPSet[A]
    // intersection
    def &(anotherSet: MyPSet[A]): MyPSet[A]

    // negation
    def unary_! : MyPSet[A]

}


class EmptyPSet[A] extends MyPSet[A] {

    override def contains(elem: A): Boolean = false

    override def +(elem: A): MyPSet[A] = new NonEmptyPSet[A](elem, this)

    override def ++(anotherSet: MyPSet[A]): MyPSet[A] = anotherSet

    override def map[B](f: A => B): MyPSet[B] = new EmptyPSet[B]

    override def flatMap[B](f: A => MyPSet[B]): MyPSet[B] = new EmptyPSet[B]

    override def filter(predicate: A => Boolean): MyPSet[A] = this

    override def foreach(f: A => Unit): Unit = ()

    override def -(elem: A): MyPSet[A] = this

    override def --(anotherSet: MyPSet[A]): MyPSet[A] = this

    override def &(anotherSet: MyPSet[A]): MyPSet[A] = this

    override def unary_! : MyPSet[A] = new PropertyBasedSet[A](_ => true)

}

class NonEmptyPSet[A](head: A, tail: MyPSet[A]) extends MyPSet[A] {

    override def contains(elem: A): Boolean = elem == head || tail.contains(elem)

    override def +(elem: A): MyPSet[A] =
        if (this contains elem) this
        else new NonEmptyPSet[A](elem, this)

    override def ++(anotherSet: MyPSet[A]): MyPSet[A] = tail ++ anotherSet + head

    override def map[B](f: A => B): MyPSet[B] = (tail map f) + f(head)

    override def flatMap[B](f: A => MyPSet[B]): MyPSet[B] = (tail flatMap f) ++ f(head)

    override def filter(predicate: A => Boolean): MyPSet[A] = {
        val filteredTail = tail filter predicate
        if (predicate(head)) filteredTail + head
        else filteredTail
    }

    override def foreach(f: A => Unit): Unit = {
        f(head)
        tail foreach f
    }

    override def -(elem: A): MyPSet[A] =
        if (head == elem) tail
        else (tail - elem) + head

    override def --(anotherSet: MyPSet[A]): MyPSet[A] = filter(x => !anotherSet(x))

    override def &(anotherSet: MyPSet[A]): MyPSet[A] = filter(anotherSet)

    override def unary_! : MyPSet[A] = new PropertyBasedSet[A](x => !this.contains(x))

}

// all elements of type A which satisfy a property
// {x in A : property(x) }
class PropertyBasedSet[A](property: A => Boolean) extends MyPSet[A] {

    def contains(elem: A): Boolean = property(elem)

    // add
    // {x in A | property(x) } + elem = {x in A | property(x) || x == elem}
    def +(elem: A): MyPSet[A] =
        new PropertyBasedSet[A](x => property(x) || x == elem)

    // union
    // {x in A | property(x) } ++ set = {x in A | property(x) || set contains x}
    def ++(anotherSet: MyPSet[A]): MyPSet[A] =
        new PropertyBasedSet[A](x => property(x) || anotherSet(x))

    def map[B](f: A => B): MyPSet[B] = politelyFail
    def flatMap[B](f: A => MyPSet[B]): MyPSet[B] = politelyFail
    def foreach(f: A => Unit): Unit = politelyFail

    def filter(predicate: A => Boolean): MyPSet[A] =
        new PropertyBasedSet[A](x => property(x) && predicate(x))

    // remove
    def -(elem: A): MyPSet[A] = filter(x => x != elem)
    // difference
    def --(anotherSet: MyPSet[A]): MyPSet[A] = filter(!anotherSet)
    // intersection
    def &(anotherSet: MyPSet[A]): MyPSet[A] = filter(anotherSet)

    // negation
    def unary_! : MyPSet[A] = new PropertyBasedSet[A](x => !property(x))

    def politelyFail = throw new IllegalArgumentException("really deep rabbit hole")

}


object MyPSet {

    def apply[A](values: A*): MyPSet[A] = {
        @tailrec
        def buildSet(valSeq: Seq[A], acc: MyPSet[A]): MyPSet[A] = {
            if (valSeq.isEmpty) acc
            else buildSet(valSeq.tail, acc+ valSeq.head)
        }

        buildSet(values.toSeq, new EmptyPSet[A])
    }

}


object PotentiallyInfiniteSetPlayground extends App {

    val s: MyPSet[Int] = MyPSet(1,2,3,4)

    val negative: MyPSet[Int] = !s // s.unary_!

    println(negative(2))
    println(negative(5))

    val negativeEven: MyPSet[Int] = negative.filter(_ % 2 == 0)

    println(negativeEven(5))

    val negativeEven5: MyPSet[Int] = negativeEven + 5

    println(negativeEven5(5))

}