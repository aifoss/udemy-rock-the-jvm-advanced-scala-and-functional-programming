package com.learning.scala.lectures.section05.implicits

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _40_TypeClassUseCase_TheMagnetPattern extends App {

    // method overloading

    /*
        Issues:
        1. type erasure
        2. lifting doesn't work for all overloads
        3. code duplication
        4. type inference and default args
     */

    class P2PRequest
    class P2PResponse
    class Serializer[T]

    trait Actor {
        def receive(statusCode: Int): Int
        def receive(request: P2PRequest): Int
        def receive(response: P2PResponse): Int
        def receive[T: Serializer](message: T): Int
        def receive[T: Serializer](message: T, statusCode: Int): Int
        def receive(future: Future[P2PRequest]): Int
    }


    /* Magnet Pattern */

    trait MessageMagnet[Result] {
        def apply(): Result
    }

    def receive[R](magnet: MessageMagnet[R]): R = magnet()

    implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
        override def apply(): Int = {
            println("handling P2P request")
            42
        }
    }

    implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
        override def apply(): Int = {
            println("handling P2P response")
            24
        }
    }

    receive(new P2PRequest)
    receive(new P2PResponse)


    /*
        Benefits of Magnet Pattern
     */

    // 1 - no more type erasure problem

    implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
        override def apply(): Int = 2
    }

    implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
        override def apply(): Int = 3
    }

    println(receive(Future(new P2PRequest)))
    println(receive(Future(new P2PResponse)))

    // 2 - lifting works

    trait AddMagnet {
        def apply(): Int
    }

    def add1(magnet: AddMagnet): Int = magnet()

    implicit class AddInt(x: Int) extends AddMagnet {
        override def apply(): Int = x + 1
    }

    implicit class AddString(s: String) extends AddMagnet {
        override def apply(): Int = s.toInt + 1
    }

    val addFV = add1 _
    println(addFV(1))
    println(addFV("3"))


    /*
        Drawbacks of Magnet Pattern
        1 - verbose
        2 - harder to read
        3 - you can't name or place default arguments
        4 - call-by-name doesn't work correctly
     */

    class Handler {
        def handle(s: => String) = {
            println(s)
            println(s)
        }
    }

    trait HandleMagnet {
        def apply(): Unit
    }

    def handle(magnet: HandleMagnet) = magnet()

    implicit class StringHandle(s: => String) extends HandleMagnet {
        override def apply(): Unit = {
            println(s)
            println(s)
        }
    }

    def sideEffectMethod(): String = {
        println("Hello, Scala")
        "hahaha"
    }

    handle(sideEffectMethod())

    handle {
        println("Hello, Scala")
        "hahaha" // new StringHandle("hahaha")
    }

}
