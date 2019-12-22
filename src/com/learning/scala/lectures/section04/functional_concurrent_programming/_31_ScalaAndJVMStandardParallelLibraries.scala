package com.learning.scala.lectures.section04.functional_concurrent_programming

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference

//import scala.collection.parallel.immutable.ParVector

/**
 * Created by sofia on 2019-10-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _31_ScalaAndJVMStandardParallelLibraries extends App {

    // 1. parallel collections

//    var aParList = List(1,2,3).par

//    var aParVector = ParVector[Int](1,2,3)

    def measure[T](operation: => T): Long = {
        val time = System.currentTimeMillis()
        operation
        System.currentTimeMillis() - time
    }

    val list = (1 to 10000000).toList

    val serialTime = measure {
        list.map(_ + 1)
    }

    val parallelTime = measure {
//        list.par.map(_ + 1)
    }

    println("serial time: "+serialTime)
    println("parallel time: "+parallelTime)

    /*
        map-reduce model
        - split the elements into chunks - Splitter
        - operation
        - recombine - Combiner
     */

    // map, flatMap, filter, foreach, reduce, fold

    // fold, reduce with non-associative operators
    println(List(1,2,3).reduce(_ - _))
//    println(List(1,2,3).par.reduce(_ - _))

    // synchronization
    var sum = 0
//    List(1,2,3).par.foreach(sum += _)
    println(sum) // race condition

    // configuring
//    aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
//    /*
//        alternatives
//        - ThreadPoolTaskSupport - deprecated
//        - ExecutionContextTaskSupport(EC)
//     */
//    aParVector.tasksupport = new TaskSupport {
//        override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???
//
//        override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???
//
//        override def parallelismLevel: Int = ???
//
//        override val environment: AnyRef = ???
//    }


    // 2. atomic ops and references

    val atomic = new AtomicReference[Int](2)

    val currentValue = atomic.get() // thread-safe read
    atomic.set(4) // thread-safe write
    atomic.getAndSet(5) // thread-safe combo

    atomic.compareAndSet(38, 56)
    // if value is 38, set it to 56
    // reference equality

    atomic.updateAndGet(_ + 1) // thread-safe function run
    atomic.getAndUpdate(_ + 1)

    atomic.accumulateAndGet(12, _ + _) // thread-safe accumulation
    atomic.getAndAccumulate(12, _ + _)

}
