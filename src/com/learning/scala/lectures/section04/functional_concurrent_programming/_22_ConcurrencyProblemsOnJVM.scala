package com.learning.scala.lectures.section04.functional_concurrent_programming

/**
 * Created by sofia on 2019-10-19.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _22_ConcurrencyProblemsOnJVM extends App {

    // race condition
    def runInParallel = {
        var x = 0

        val thread1 = new Thread(() => {
            x = 1
        })

        val thread2 = new Thread(() => {
            x = 2
        })

        thread1.start()
        thread2.start()

//        println(x)
    }

    for (_ <- 1 to 1000) runInParallel

    class BankAccount(var amount: Int) {
        override def toString: String = "" + amount
    }

    def buy(account: BankAccount, thing: String, price: Int): Unit = {
        account.amount -= price
    }

    def buyThings: Unit = {
        val account = new BankAccount(50000)
        val thread1 = new Thread(() => buy(account, "shoes", 3000))
        val thread2 = new Thread(() => buy(account, "iphone12", 4000))

        thread1.start()
        thread2.start()

        Thread.sleep(10)

        if (account.amount != 43000) println("AHA: "+account.amount)

        //        println()
    }

    for (_ <- 1 to 1000) buyThings

    // option #1: use synchronized - preferable
    def buySafe(account: BankAccount, thing: String, price: Int): Unit = {
        account.synchronized {
            // no two threads can evaluate this at the same time
            account.amount -= price
            println("I've bought "+thing)
            println("My account balance is now "+account)
        }
    }

    // option #2: use @volatile
    class SafeBankAccount(@volatile var amount: Int) {
        override def toString: String = "" + amount
    }


    /* Exercises */

    // 1. construct 50 "inception" threads
    //    thread1 -> thread2 -> thread3 -> ...
    //    println("hello from thread #")
    //    in reverse order

    def inceptionThreads(maxThreads: Int, idx: Int = 1): Thread = new Thread(() => {
        if (idx < maxThreads) {
            val newThread = inceptionThreads(maxThreads, idx+1)
            newThread.start()
            newThread.join()
        }

        println(s"Hello from thread $idx")
    })

    inceptionThreads(50).start()

    // 2. what is the biggest value possible for x? 100
    //    what is the smallest value possible for x? 1
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))

    threads.foreach(_.start())
    threads.foreach(_.join())

    println(x)

    // 3. sleep fallacy
    //    what's the value of message? almost always "Scala is awesome"
    //    is it guaranteed? no
    //    why or why not?
    /*
            (main thread)
                message = "Scala sucks"
                awesomeThread.start()
                sleep() - relieves execution
            (awesome thread)
                sleep() - relieves execution
            (OS gives CPU to some important thread - takes CPU for more than 2 seconds)
            (OS gives CPU back to MAIN thread)
                println("Scala sucks")
            (OS gives CPU to awesome thread)
                message = "Scala is awesome"
     */
    var message = ""
    val awesomeThread = new Thread(() => {
        Thread.sleep(1000)
        message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    awesomeThread.join() // how to fix: wait for the awesome thread to finish
    println(message)

}
