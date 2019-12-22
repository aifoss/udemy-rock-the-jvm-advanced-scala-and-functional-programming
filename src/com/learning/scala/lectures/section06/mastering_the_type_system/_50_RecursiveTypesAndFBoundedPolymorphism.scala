package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-12-21.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _50_RecursiveTypesAndFBoundedPolymorphism extends App {

    // how to make the compiler enforce correct types?

    // solution 1 - naive

//    trait Animal {
//        def breed: List[Animal]
//    }
//
//    class Cat extends Animal {
//        override def breed: List[Cat] = ???
//    }
//
//    class Dog extends Animal {
//        override def breed: List[Dog] = ???
//    }


    // solution 2
    // recursive type: F-bounded polymorphism

//    trait Animal[A <: Animal[A]] {
//        def breed: List[Animal[A]]
//    }
//
//    class Cat extends Animal[Cat] {
//        override def breed: List[Animal[Cat]] = ???
//    }
//
//    class Dog extends Animal[Dog] {
//        override def breed: List[Animal[Dog]] = ???
//    }
//
//    trait Entity[E <: Entity[E]] // ORM
//    class Person extends Comparable[Person] {
//        override def compareTo(o: Person): Int = ???
//    }
//
//    class Crocodile extends Animal[Dog] { // not prevented
//        override def breed: List[Animal[Dog]] = ???
//    }


    // solution 3
    // FBP + self-type

//    trait Animal[A <: Animal[A]] { self: A =>
//        def breed: List[Animal[A]]
//    }
//
//    class Cat extends Animal[Cat] {
//        override def breed: List[Animal[Cat]] = ???
//    }
//
//    class Dog extends Animal[Dog] {
//        override def breed: List[Animal[Dog]] = ???
//    }
//
//    trait Entity[E <: Entity[E]] // ORM
//    class Person extends Comparable[Person] {
//        override def compareTo(o: Person): Int = ???
//    }

//    class Crocodile extends Animal[Dog] { // prevented
//        override def breed: List[Animal[Dog]] = ???
//    }

//    trait Fish extends Animal[Fish]
//    class Shark extends Fish {
//        override def breed: List[Animal[Fish]] = List(new Cod) // wrong
//    }
//    class Cod extends Fish {
//        override def breed: List[Animal[Fish]] = ???
//    }


    /*
        Exercise
     */

    // solution 4: type classes

//    trait Animal
//    trait CanBreed[A] {
//        def breed(a: A): List[A]
//    }
//
//    class Dog extends Animal
//    object Dog {
//        implicit object DogsCanBreed extends CanBreed[Dog] {
//            override def breed(a: Dog): List[Dog] = ???
//        }
//    }
//
//    implicit class CanBreedOps[A](animal: A) {
//        def breed(implicit canBreed: CanBreed[A]): List[A] = {
//            canBreed.breed(animal)
//        }
//    }
//
//    val dog = new Dog
//    dog.breed // new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)

    // this is prevented
//    class Cat extends Animal
//    object Cat {
//        implicit object CatsCanBreed extends CanBreed[Dog] {
//            def breed(a: Dog): List[Dog] = List()
//        }
//    }
//
//    val cat = new Cat
//    cat.breed


    // solution 5

    trait Animal[A] {
        def breed(a: A): List[A]
    }

    class Dog
    object Dog {
        implicit object DogAnimal extends Animal[Dog] {
            override def breed(a: Dog): List[Dog] = ???
        }
    }

    implicit class AnimalOps[A](animal: A) {
        def breed(implicit animalTypeClassInstance: Animal[A]): List[A] = {
            animalTypeClassInstance.breed(animal)
        }
    }

    val dog = new Dog
    dog.breed


}
