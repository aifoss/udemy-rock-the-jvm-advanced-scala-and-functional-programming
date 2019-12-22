package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-11-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _46_TypeMembers extends App {

    class Animal
    class Dog extends Animal
    class Cat extends Animal

    class AnimalCollection {
        type AnimalType
        type BoundedAnimal <: Animal
        type SuperBoundedAnimal >: Dog <: Animal
        type AnimalC = Cat
    }

    val ac = new AnimalCollection

//    val dog: ac.AnimalType = ???
//    val cat: ac.BoundedAnimal = ???

    val pup: ac.SuperBoundedAnimal = new Dog
    val cat: ac.AnimalC = new Cat

    type CatAlias = Cat

    val anotherCat: CatAlias = new Cat

    // alternative to generics
    trait MyList {
        type T
        def add(element: T): MyList
    }

    class NonEmptyList(value: Int) extends MyList {
        override type T = Int
        def add(element: Int): MyList = ???
    }

    // .type
    type CatsType = cat.type
    val newCat: CatsType = cat

    /*
        Exercise:
        Enforce a type to be applicable to SOME types only.
     */

    // locked
    trait MList {
        type A
        def head: A
        def tail: MList
    }

    trait ApplicableToNumbers {
        type A <: Number
    }

    // should not be OK
//    class CustomList(hd: String, tl: CustomList) extends MList {
//        type A = String
//        def head = hd
//        def tail = tl
//    }

    // should be OK
    class IntList(hd: Int, tl: IntList) extends MList {
        type A = Int
        def head = hd
        def tail = tl
    }



}
