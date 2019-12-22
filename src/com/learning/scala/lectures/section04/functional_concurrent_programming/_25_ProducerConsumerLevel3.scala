package com.learning.scala.lectures.section04.functional_concurrent_programming

import scala.collection.mutable
import scala.util.Random

/**
 * Created by sofia on 2019-10-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _25_ProducerConsumerLevel3 extends App {

    class Producer(id: Int, capacity: Int, buffer: mutable.Queue[Int]) extends Thread {

        override def run(): Unit = {
            val random = new Random()
            var i = 0

            while (true) {
                buffer.synchronized {
                    while (buffer.size == capacity) {
                        println(s"[producer $id] buffer is full, waiting ...")
                        buffer.wait()
                    }

                    val x = i+1

                    buffer.enqueue(x)

                    println(s"[producer $id] i have produced "+x)

                    buffer.notify()

                    i += 1
                }

                Thread.sleep(random.nextInt(500))
            }
        }
    }

    class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {

        override def run(): Unit = {
            val random = new Random()

            while (true) {
                buffer.synchronized {
                    while (buffer.isEmpty) {
                        println(s"[consumer $id] buffer is empty, waiting ...")
                        buffer.wait()
                    }

                    val x = buffer.dequeue()

                    println(s"[consumer $id] i have consumed "+x)

                    buffer.notify()
                }

                Thread.sleep(random.nextInt(500))
            }
        }
    }

    def multipleProducersConsumers(nProducers: Int, nConsumers: Int): Unit = {
        val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
        val capacity = 20

        (1 to nProducers).foreach(i => new Producer(i, capacity, buffer).start())
        (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    }

    multipleProducersConsumers(3, 3)

}
