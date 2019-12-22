package com.learning.scala.lectures.section04.functional_concurrent_programming

import java.util.concurrent.Executors

/**
 * Created by sofia on 2019-10-19.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _21_IntroToParallelProgrammingOnJVM extends App {

    // JVM threads
    val runnable = new Runnable {
        override def run(): Unit = println("Running in parallel")
    }
    val aThread = new Thread(runnable)

    aThread.start() // gives the signal to the JVM to start a JVM thread
    runnable.run() // doesn't do anything in parallel
    aThread.join() // blocks until aThread finishes running

    val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
    val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))

    threadHello.start()
    threadGoodbye.start()

    // executors
    val pool = Executors.newFixedThreadPool(10)
    pool.execute(() => println("something in the thread pool"))

    pool.execute(() => {
        Thread.sleep(1000)
        println("done after 1 second")
    })

    pool.execute(() => {
        Thread.sleep(1000)
        println("almost done")
        Thread.sleep(1000)
        println("done after 2 seconds")
    })

    pool.shutdown()
//    pool.execute(() => println("should not appear")) // throws an exception in the calling thread

//    pool.shutdownNow() // throws InterruptedException - sleep interrupted

    println(pool.isShutdown) // true

}
