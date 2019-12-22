package com.learning.scala.lectures.section04.functional_concurrent_programming

/**
 * Created by sofia on 2019-10-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _23_JVMThreadCommunication extends App {

    class SimpleContainer {
        private var value: Int = 0

        def isEmpty: Boolean = value == 0

        def get: Int = {
            val result = value
            value = 0
            result
        }

        def set(newValue: Int): Unit = value = newValue
    }

    def naiveProducerConsumer(): Unit = {
        val container = new SimpleContainer

        val producer = new Thread(() => {
            println("[producer] computing ...")

            Thread.sleep(500)

            val value = 42

            println("[producer] i have produced, after long work, the value "+value)

            container.set(value)
        })

        val consumer = new Thread(() => {
            println("[consumer] waiting ...")

            while (container.isEmpty) {
                println("[consumer] actively waiting")
            }

            println("[consumer] i have consumed "+container.get)
        })

        producer.start()
        consumer.start()
    }

    naiveProducerConsumer()

    // wait and notify

    def smartProducerConsumer(): Unit = {
        val container = new SimpleContainer

        val producer = new Thread(() => {
            println("[producer] hard at work ...")

            Thread.sleep(2000)

            val value = 42

            container.synchronized {
                println("[producer] i'm producing "+value)
                container.set(value)
                container.notify()
            }
        })

        val consumer = new Thread(() => {
            println("[consumer] waiting ...")

            container.synchronized({
                container.wait()
            })

            println("[consumer] i have consumed "+container.get)
        })

        producer.start()
        consumer.start()
    }

    smartProducerConsumer()

}
