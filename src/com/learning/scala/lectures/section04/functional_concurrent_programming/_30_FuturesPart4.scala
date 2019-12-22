package com.learning.scala.lectures.section04.functional_concurrent_programming

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Random, Try}

/**
 * Created by sofia on 2019-10-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _30_FuturesPart4 extends App {

    /*
        Exercises

        1. Fulfill a future immediately with a value.
        2. Process two futures in sequence: inSequence(fa, fb).
        3. first(fa, fb) => new future with the first value of the two futures.
        4. last(fa, fb) => new future with the last value of the two futures.
        5. retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]

     */


    // 1. Fulfil a future immediately.

    def fulfillImmediately[T](value: T): Future[T] = Future(value)


    // 2. inSequence(fa, fb)

    def inSequence[A,B](fa: Future[A], fb: Future[B]): Future[B] =
        fa.flatMap(_ => fb)


    // 3. first(fa, fb)

    def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
        val promise = Promise[A]

//        def tryComplete(promise: Promise[A], result: Try[A]) = result match {
//            case Success(r) => try {
//                promise.success(r)
//            } catch {
//                case _ =>
//            }
//            case Failure(t) => try {
//                promise.failure(t)
//            } catch {
//                case _ =>
//            }
//        }
//
//        fa.onComplete(result => tryComplete(promise, result))
//        fb.onComplete(tryComplete(promise, _))

        fa.onComplete(promise.tryComplete)
        fb.onComplete(promise.tryComplete)

        promise.future
    }


    // 4. last(fa, fb)

    def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
        // 1 promise which both futures will try to complete
        // 2 promise which will be completed by the last future
        val bothPromise = Promise[A]
        val lastPromise = Promise[A]

        def checkAndComplete: Try[A] => Any = (result: Try[A]) =>
            if (!bothPromise.tryComplete(result))
                lastPromise.complete(result)

        fa.onComplete(checkAndComplete)
        fb.onComplete(checkAndComplete)

        lastPromise.future
    }

    val fast = Future {
        Thread.sleep(100)
        42
    }

    val slow = Future {
        Thread.sleep(200)
        45
    }

    first(fast, slow).foreach(f => println("first: "+f))
    last(fast, slow).foreach(l => println("last: "+l))

    Thread.sleep(1000)


    // 5. retryUntil

    def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] = {
        action()
            .filter(condition)
            .recoverWith {
                case _ => retryUntil(action, condition)
            }
    }

    val random = new Random()

    val action = () => Future {
        Thread.sleep(100)
        val nextValue = random.nextInt(100)
        println("generated "+nextValue)
        nextValue
    }

    retryUntil(action, (x: Int) => x < 50).foreach(result => println("settled at "+result))

    Thread.sleep(10000)

}
