package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-12-21.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _48_StructuralTypesAndCompileTimeDuckTyping extends App {

    // structural types

    type JavaCloseable = java.io.Closeable

    class HipsterCloseable {
        def close(): Unit = println("yeah yeah I'm closing")
        def closeSilently(): Unit = println("not making a sound")
    }

    type UnifiedCloseable = {
        def close(): Unit
    }

    def closeQuietly(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close()

    closeQuietly(new JavaCloseable {
        override def close(): Unit = ???
    })

    closeQuietly(new HipsterCloseable)


    // type refinements

    type AdvancedCloseable = JavaCloseable {
        def closeSilently(): Unit
    }

    class AdvancedJavaCloseable extends JavaCloseable {
        override def close(): Unit = println("Java closes")
        def closeSilently(): Unit = println("Java closes silently")
    }

    def closeShh(advancedCloseable: AdvancedCloseable): Unit = advancedCloseable.closeSilently()

    closeShh(new AdvancedJavaCloseable)
//    closeShh(new HipsterCloseable) // does not work because it does not originate from JavaCloseable


    // using structural types as standalone types

    def altClose(closeable: { def close(): Unit }): Unit = closeable.close()


    // type-checking (static duck typing)
    // caveat: based on reflection

    type SoundMaker = {
        def makeSound(): Unit
    }

    class Dog {
        def makeSound(): Unit = println("bark!")
    }

    class Car {
        def makeSound(): Unit = println("vrooom!")
    }

    val dog: SoundMaker = new Dog
    val car: SoundMaker = new Car


    /*
        Exercises
     */

    // 1. Is f compatible with CBL and Human?

    trait CBL[+T] {
        def head: T
        def tail: CBL[T]
    }

    class Human {
        def head: Brain = new Brain
    }

    class Brain {
        override def toString: String = "BRAINZ!"
    }

    def f[T](somethingWithAHead: { def head: T }): Unit = println(somethingWithAHead.head)

    case object CBNil extends CBL[Nothing] {
        def head: Nothing = ???
        def tail: CBL[Nothing] = ???
    }

    case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]

    f(CBCons(2, CBNil))
    f(new Human) // T = Brain


    // 2. Is HeadEqualizer compatible with CBL and Human?

    object HeadEqualizer {
        type Headable[T] = { def head: T }
        def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
    }

    val brainzlist = CBCons(new Brain, CBNil)
    val stringsList = CBCons("Brainz", CBNil)

    HeadEqualizer.===(brainzlist, new Human)

    HeadEqualizer.===(new Human, stringsList) // not type-safe

}
