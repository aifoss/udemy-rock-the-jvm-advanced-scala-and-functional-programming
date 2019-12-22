package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-12-21.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _49_SelfTypes extends App {

    // requiring a type to be mixed in

    trait Instrumentalist {
        def play(): Unit
    }

    trait Singer { self: Instrumentalist => // whoever implements Singer to implement Instrumentalist
        def sing(): Unit
    }

    class LeadSinger extends Singer with Instrumentalist {
        override def play(): Unit = ???
        override def sing(): Unit = ???
    }

    // illegal
//    class Vocalist extends Singer {
//        override def sing(): Unit = ???
//    }

    val jamesHetfield = new Singer with Instrumentalist {
        override def play(): Unit = ???
        override def sing(): Unit = ???
    }

    class Guitarist extends Instrumentalist {
        override def play(): Unit = println("(guitar solo)")
    }

    val ericClapton = new Guitarist with Singer {
        override def sing(): Unit = ???
    }


    // self-type vs inheritance

    class A
    class B extends A // B is A

    trait T
    trait S { self: T => } // S requires T


    // cake pattern vs dependency injection

    // dependency injection

    class Component {
        // API
    }
    class ComponentA extends Component
    class ComponentB extends Component
    class DependentComponent(val component: Component)

    // cake pattern

    trait ScalaComponent {
        // API
        def action(x: Int): String
    }

    trait ScalaDependentComponent { self: ScalaComponent =>
        def dependentAction(x: Int): String = action(x) + " this rocks!"
    }

    trait ScalaApplication { self: ScalaDependentComponent => }

    // layer 1 - small components
    trait Picture extends ScalaComponent
    trait Stats extends ScalaComponent

    // layer 2 - compose
    trait Profile extends ScalaDependentComponent with Picture
    trait Analytics extends ScalaDependentComponent with Stats

    // layer 3
    trait AnalyticsApp extends ScalaApplication with Analytics


    // cyclical dependency

    trait X { self: Y => }
    trait Y { self: X => }

}
