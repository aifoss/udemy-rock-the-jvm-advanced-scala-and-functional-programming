package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-12-21.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _47_InnerTypesAndPathDependentTypes extends App {

    class Outer {
        class Inner
        object InnerObject
        type InnerType

        def print(i: Inner): Unit = println(i)
        def printGeneral(i: Outer#Inner): Unit = println(i)
    }

    def aMethod: Int = {
        class HelperClass
        type HelperType = String // type inside other than class can only be an alias
        2
    }

    // per-instance
    val o = new Outer
    val inner = new o.Inner // o.Inner is a type

    val oo = new Outer
//    val otherInner: oo.Inner = new o.Inner // does not compile

    o.print(inner)
//    oo.print(inner) // does not compile

    // path-dependent types

    // Outer#Inner
    o.printGeneral(inner)
    oo.printGeneral(inner)


    /*
        Exercise
        DB keyed by Int or String, but maybe others
     */

    trait ItemLike {
        type Key
    }

    trait Item[K] extends ItemLike {
        type Key = K
    }

    trait IntItem extends Item[Int]
    trait StringItem extends Item[String]

    def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

    get[IntItem](42)
    get[StringItem]("home")

//    get[IntItem]("scala") // should not be ok

}
