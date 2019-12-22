package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _43_AdvancedInheritance extends App {

    // convenience

    trait Writer[T] {
        def write(value: T): Unit
    }

    trait Closeable {
        def close(status: Int): Unit
    }

    trait GenericStream[T] {
        def foreach(f: T => Unit): Unit
    }

    def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
        stream.foreach(println)
        stream.close(0)
    }

    // diamond problem
    // last override gets picked

    trait Animal {
        def name: String
    }
    trait Lion extends Animal {
        override def name: String = "lion"
    }
    trait Tiger extends Animal {
        override def name: String = "tiger"
    }
    class Mutant extends Lion with Tiger {
//        override def name: String = "ALIEN"
    }

    val mutant = new Mutant
    println(mutant.name)

    // super problem + type linearization

    trait Cold {
        def print: Unit = println("cold")
    }

    trait Green extends Cold {
        override def print: Unit = {
            println("green")
            super.print
        }
    }

    trait Blue extends Cold {
        override def print: Unit = {
            println("blue")
            super.print
        }
    }

    class Red {
        def print: Unit = println("red")
    }

    class White extends Red with Green with Blue {
        override def print: Unit = {
            println("white")
            super.print
        }
    }

    val color = new White
    color.print

}
