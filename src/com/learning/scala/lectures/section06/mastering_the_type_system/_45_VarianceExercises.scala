package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _45_VarianceExercises extends App {

    /*
        1. Implement invariant, covariant, contravariant versions:
            Parking[T](things: List[T}) {
                park(vehicle: T)
                impound(vehicles: List[T])
                checkVehicles(conditions: String): List[T]

        2. Use someone else's API: IList[T]

        3. Parking = monad!
            - flatMap
     */


    class Vehicle
    class Bike extends Vehicle
    class Car extends Vehicle
    class IList[T]


    // 1. implement invariant, covariant, and contravariant versions of the API
    // 3. flatMap

    class IParking[T](vehicles: List[T]) {
        def park(vehicle: T): IParking[T] = ???
        def impound(vehicles: List[T]): IParking[T] = ???
        def checkVehicles(conditions: String): List[T] = ???
        def flatMap[S](f: T => IParking[S]): IParking[S] = ???
    }

    class CoParking[+T](vehicles: List[T]) {
        def park[S >: T](vehicle: S): CoParking[S] = ???
        def impound[S >: T](vehicles: List[S]): CoParking[S] = ???
        def checkVehicles(conditions: String): List[T] = ???
        def flatMap[S](f: T => CoParking[S]): CoParking[S] = ???
    }

    class ContraParking[-T](vehicles: List[T]) {
        def park(vehicles: T): ContraParking[T] = ???
        def impound(vehicles: List[T]): ContraParking[T] = ???
        def checkVehicles[C <: T](conditions: String): List[C] = ???
        def flatMap[R <: T, S](f : Function[R, ContraParking[S]]): ContraParking[S] = ???
    }

    /*
        Rule of Thumb
        - use covariance for collection of things
        - use contravariance for group of actions
     */


    // 2. use IList[T]

    class CoParking2[+T](vehicles: IList[T]) {
        def park[S >: T](vehicle: S): CoParking2[S] = ???
        def impound[S >: T](vehicles: IList[S]): CoParking2[S] = ???
        def checkVehicles[S >: T](conditions: String): IList[S] = ???
    }

    class ContraParking2[-T](vehicles: IList[T]) {
        def park(vehicles: T): ContraParking2[T] = ???
        def impound[C <: T](vehicles: IList[C]): ContraParking2[C] = ???
        def checkVehicles[C <: T](conditions: String): IList[C] = ???
    }

}
