package com.learning.scala.lectures.section04.functional_concurrent_programming

/**
 * Created by sofia on 2019-10-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _26_JVMThreadCommunicationsExercises extends App {

    /*
        Exercises

        1. Think of an example where notifyAll acts in a different way than notify.
        2. Create a deadlock.
        3. Create a livelock.
     */

    // 1. notifyAll

    def testNotifyAll(): Unit = {
        val bell = new Object

        (1 to 10).foreach(i => new Thread(() => {
            bell.synchronized {
                println(s"[thread $i] waiting ...")
                bell.wait()
                println(s"[thread $i] hooray!")
            }
        }).start())

        new Thread(() => {
            Thread.sleep(2000)
            println("[announcer] Rock'n roll!")

            bell.synchronized {
                bell.notifyAll()
            }
        }).start()
    }

    testNotifyAll()

    // 2. deadlock

    case class Friend(name: String) {
        def bow(other: Friend): Unit = {
            this.synchronized {
                println(s"$this: I am bowing to my friend $other")
                other.rise(this)
                println(s"$this: my friend $other has risen")
            }
        }

        def rise(other: Friend): Unit = {
            this.synchronized {
                println(s"$this: I am rising to my friend $other")
            }
        }

        var side = "right"

        def switchSide(): Unit = {
            if (side == "right") side = "left"
            else side = "right"
        }

        def pass(other: Friend): Unit = {
            while (this.side == other.side) {
                println(s"$this: Oh, but please, $other, feel free to pass ...")
                switchSide()
                Thread.sleep(1000)
            }
        }
    }

    val sam = Friend("Sam")
    val pierre = Friend("Pierre")

    new Thread(() => sam.bow(pierre)).start() // sam's lock, then pierre's lock
    new Thread(() => pierre.bow(sam)).start() // pierre's lock, then sam's lock

    // 3. livelock

    new Thread(() => sam.pass(pierre)).start()
    new Thread(() => pierre.pass(sam)).start()

}
