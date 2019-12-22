package com.learning.scala.lectures.section04.functional_concurrent_programming

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by sofia on 2019-10-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _27_FuturesAndPromises extends App {

    def calculateMeaningOfLife: Int = {
        Thread.sleep(2000)
        42
    }

    val aFuture = Future {
        calculateMeaningOfLife // calculates meaning of life on another thread
    } // (global passed by compiler)

    println(aFuture.value) // Option[Try[Int]]

    println("waiting on the future")

//    aFuture.onComplete(t => t match {
//        case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
//        case Failure(e) => println(s"i have failed with $e")
//    })

    aFuture.onComplete {
        case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
        case Failure(e) => println(s"i have failed with $e")
    }

    Thread.sleep(3000)

}
