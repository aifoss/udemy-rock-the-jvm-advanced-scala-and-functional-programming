package com.learning.scala.lectures.section04.functional_concurrent_programming

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.util.Success

/**
 * Created by sofia on 2019-10-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _29_FuturesPart3 extends App {

    case class User(name: String)

    case class Transaction(sender: String, receiver: String, amount: Double, status: String)

    object BankingApp {

        val name = "Rock the JVM Banking"

        def fetchUser(name: String): Future[User] = Future {
            Thread.sleep(500)
            User(name)
        }

        def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
            Thread.sleep(1000)
            Transaction(user.name, merchantName, amount, "SUCCESS")
        }

        def purchase(userName: String, item: String, merchantName: String, cost: Double): String = {
            val transactionStatusFuture = for {
                user <- fetchUser(userName)
                transaction <- createTransaction(user, merchantName, cost)
            } yield transaction.status

            Await.result(transactionStatusFuture, 2.seconds)
        }

    }

    println(BankingApp.purchase("John", "iPhone 12", "Rock the JVM Store", 3000))


    // promises

    val promise = Promise[Int]()
    val future = promise.future

    val producer = new Thread(() => {
        println("[producer] crunching numbers ...")
        Thread.sleep(500)
        // "fulfilling" the promise
        promise.success(42)
        println("[producer] done")
    })

    // consumer
    future.onComplete {
        case Success(r) => println("[consumer] i have received "+r)
    }

    producer.start()

    Thread.sleep(1000)

}
