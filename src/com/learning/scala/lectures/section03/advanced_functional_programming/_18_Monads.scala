package com.learning.scala.lectures.section03.advanced_functional_programming

/**
 * Created by sofia on 2019-10-05.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _18_Monads extends App {

    // Monad is a kind of type which has some fundamental ops
    // -- unit (also called pure or apply)
    // -- flatMap (also called bind)

    // List, Option, Try, Future, Stream, Set are all monads

    // Operations must satisfy the monad laws:

    // 1. left identity
    // unit(x).flatMap(f) == f(x)

    // 2. right identity
    // aMonadInstance.flatMap(unit) == aMonadInstance

    // 3. associativity
    // m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))


    /* List */

    // left identity:
    // List(x).flatMap(f) =
    // f(x) ++ flatMap(f) =
    // f(x)

    // right identity:
    // list.flatMap(x => List(x)) =
    // list

    // associativity
    // [a b c].flatMap(f).flatMap(g) =
    // (f(a) ++ f(b) ++ f(c)).flatMap(g) =
    // f(a).flatMap(g) ++ f(b).flatMap(g) ++ f(c).flatMap(g) =
    // [a b c].flatMap(f(_).flatMap(g)) =
    // [a b c].flatMap(x => f(x).flatMap(g))


    /* Option */

    // left identity
    // Option(x).flatMap(f) = f(x)
    // Some(x).flatMap(f) = f(x)

    // right identity
    // opt.flatMap(x => Option(x)) = opt
    // Some(v).flatMap(x => Option(x)) =
    // Option(v) =
    // Some(v)

    // associativity
    // o.flatMap(f).flatMap(g) =
    // o.flatMap(x => f(x).flatMap(g))
    // Some(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
    // Some(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)


    /* Our Own Try Monad */

    trait Attempt[+A] {

        def flatMap[B](f: A => Attempt[B]): Attempt[B]
    }

    object Attempt {

        def apply[A](a: => A): Attempt[A] =
            try {
                Success(a)
            } catch {
                case e: Throwable => Failure(e)
            }
    }

    case class Success[+A](value: A) extends Attempt[A] {

        override def flatMap[B](f: A => Attempt[B]): Attempt[B] =
            try {
                f(value)
            } catch {
                case e: Throwable => Failure(e)
            }
    }

    case class Failure[A](e: Throwable) extends Attempt[Nothing] {

        override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
    }

    // left identity
    // unit.flatMap(f) = f(x)
    // Attempt(x).flatMap(f) = f(x) // only in Success
    // Success(x).flatMap(f) = f(x)

    // right identity
    // attempt.flatMap(unit) = attempt
    // Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
    // Failure(e).flatMap(...) = Failure(e)

    // associativity
    // attempt.flatMap(f).flatMap(g) = attempt.flatMap(x => f(x).flatMap(g))
    // Failure(e).flatMap(f).flatMap(g) = Failure(e)
    // Failure(e).flatMap(x => f(x).flatMap(g)) = Failure(e)
    // Success(v).flatMap(f).flatMap(g) =
    // f(v).flatMap(g) OR Failure(e)
    // Success(v).flatMap(x => f(x).flatMap(g)) =
    // f(v).flatMap(g) OR Failure(e)

    val attempt = Attempt {
        throw new RuntimeException("My own monad, yes!")
    }

    println(attempt)

}
