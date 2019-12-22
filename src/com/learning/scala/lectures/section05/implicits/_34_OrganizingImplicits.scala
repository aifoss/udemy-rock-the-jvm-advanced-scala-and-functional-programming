package com.learning.scala.lectures.section05.implicits

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _34_OrganizingImplicits extends App {

    val list = List(1,4,5,2,3)

//    println(list.sorted)

    implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

    println(list.sorted)


    /*
        Implicits (used as implicit parameters):
        - val/var
        - object
        - accessor methods = defs with no parameters
     */

    case class Person(name: String, age: Int)

    val persons = List(
        Person("Steve", 30),
        Person("Amy", 22),
        Person("John", 66)
    )

//    implicit def personOrderByAge: Ordering[Person] = Ordering.fromLessThan((a,b) => a.age < b.age)
//    implicit val personOrderByName: Ordering[Person] = Ordering.fromLessThan(
//        (a,b) => a.name.compareTo(b.name) < 0
//    )
//
//    println(persons.sorted)


    /*
        Implicit Scope
        - normal scope = local scope
        - imported scope
        - companions of all types involved in the method signature
     */


    /*
        Best Practices
        1. If
            - there is a single possible value for it
            - and you can edit the code for the type
           then define the implicit in the companion
        2. If
            - there are many possible values for it
            - but there is a single good one
            - and you can edit the code for the type
           then define the good implicit in the companion
     */

    object PersonOrderingByName {
        implicit val personOrderByName: Ordering[Person] = Ordering.fromLessThan(
            (a,b) => a.name.compareTo(b.name) < 0
        )
    }

    object PersonOrderingByAge {
        implicit val personOrderByAge: Ordering[Person] = Ordering.fromLessThan((a,b) => a.age < b.age)
    }

    import PersonOrderingByAge._

    println(persons.sorted)


    /*
        Exercise:
        Implement implicit ordering
        - totalPrice (50%)
        - by unit count (25%)
        - by unit price (25%)
     */

    case class Purchase(nUnits: Int, unitPrice: Double)

    object Purchase {
        implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
    }

    object UnitCountOrdering {
        implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
    }

    object UnitPriceOrdering {
        implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
    }

}
