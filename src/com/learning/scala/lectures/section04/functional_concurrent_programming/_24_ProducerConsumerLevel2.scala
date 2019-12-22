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
object _24_ProducerConsumerLevel2 extends App {

    def producerConsumerLargeBuffer(): Unit = {
        val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
        val capacity = 3

        val producer = new Thread(() => {
            val random = new Random()
            var i = 0

            while (true) {
                buffer.synchronized {
                    if (buffer.size == capacity) {
                        println("[producer] buffer is full, waiting ...")
                        buffer.wait()
                    }

                    val x = i+1

                    buffer.enqueue(x)

                    println("[producer] i have produced "+x)

                    buffer.notify()

                    i += 1
                }

                Thread.sleep(random.nextInt(250))
            }
        })

        val consumer = new Thread(() => {
            val random = new Random()

            while (true) {
                buffer.synchronized {
                    if (buffer.isEmpty) {
                        println("[consumer] buffer is empty, waiting ...")
                        buffer.wait()
                    }

                    val x = buffer.dequeue()

                    println("[consumer] i have consumed "+x)

                    buffer.notify()
                }

                Thread.sleep(random.nextInt(500))
            }
        })

        producer.start()
        consumer.start()
    }

    producerConsumerLargeBuffer()

}
