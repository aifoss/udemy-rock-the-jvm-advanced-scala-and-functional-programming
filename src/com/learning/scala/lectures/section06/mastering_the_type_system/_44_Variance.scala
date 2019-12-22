package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _44_Variance extends App {

    trait Animal
    class Dog extends Animal
    class Cat extends Animal
    class Crocodile extends Animal

    class Cage[T]

    // covariance
    class CCage[+T]
    val ccage: CCage[Animal] = new CCage[Cat]

    // invariance
    class ICage[T]

    // contravariance
    class XCage[-T]
    val xcage: XCage[Cat] = new XCage[Animal]

    class InvariantCage[T](val animal: T)

    class CovariantCage[+T](val animal: T)

//    class ContravariantCage[-T](val animal: T)

    class InvariantVariableCage[T](var animal: T)

//    trait AnotherCovariantCage[+T] {
//        def addAnimal(animal: T)
//    }

    class AnotherContravariantCage[-T] {
        def addAnimal(animal: T) = true
    }

    val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
    acc.addAnimal(new Cat)
    class Kitty extends Cat
    acc.addAnimal(new Kitty)

    class MyList[+A] {
        def add[B >: A](element: B): MyList[B] = new MyList[B]
    }

    val emptyList = new MyList[Kitty]
    val animals = emptyList.add(new Kitty)
    val moreAnimals = animals.add(new Cat)
    val evenMoreAnimals = moreAnimals.add(new Dog)

    // return types
    class PetShop[-T] {
//        def get(isItAPuppy: Boolean): T
        def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
    }

    val shop: PetShop[Dog] = new PetShop[Animal]
//    val evilCat = shop.get(true, new Cat)

    class TerraNova extends Dog
    val bigFurry: TerraNova = shop.get(true, new TerraNova)


    /*
        Big Rule
        - method arguments are in contravariant position
        - method return types are in covariant position
     */

}
