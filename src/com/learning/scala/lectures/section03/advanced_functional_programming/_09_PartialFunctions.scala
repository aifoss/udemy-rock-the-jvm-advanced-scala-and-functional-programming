package com.learning.scala.lectures.section03.advanced_functional_programming

/**
 * Created by sofia on 2019-10-02.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _09_PartialFunctions extends App {

    val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

    val aFussyFunction = (x: Int) =>
        if (x == 1) 42
        else if (x == 2) 56
        else if (x == 5) 999
        else throw new FunctionNotApplicableException

    class FunctionNotApplicableException extends RuntimeException

    val aNicerFussyFunction = (x: Int) => x match {
        case 1 => 42
        case 2 => 56
        case 5 => 999
    }
    // {1,2,5} => Int

    val aPartialFunction: PartialFunction[Int, Int] = {
        case 1 => 42
        case 2 => 56
        case 5 => 999
    }
    // partial function value

    println(aPartialFunction(2))


    // partial function utilities

    println(aPartialFunction.isDefinedAt(67))

    val lifted = aPartialFunction.lift // Int => Option[Int]

    println(lifted(2))
    println(lifted(98))

    val pfChain = aPartialFunction.orElse[Int, Int] {
        case 45 => 67
    }

    println(pfChain(2))
    println(pfChain(45))


    // partial functions extend normal functions

    val aTotalFunction: Int => Int = {
        case 1 => 99
    }


    // higher-order functions accept partial functions as well

    val aMappedList = List(1,2,3).map {
        case 1 => 42
        case 2 => 78
        case 3 => 1000
    }

    println(aMappedList)


    // partial functions can only have one parameter type


    /* Exercises */

    // 1. construct a partial function instance (anonymous class)

    val aManualFussyFunction = new PartialFunction[Int, Int] {
        override def apply(x: Int): Int = x match {
            case 1 => 42
            case 2 => 56
            case 5 => 999
        }

        override def isDefinedAt(x: Int): Boolean =
            x == 1 || x == 2 || x == 5
    }

    // 2. dumb chatbot as a partial function

    val chatbot: PartialFunction[String, String] = {
        case "hello" => "Hi, my name is HAL9000"
        case "goodbye" => "Once you start taking to me, there is no return, human!"
        case "call mom" => "Unable to find your phone without your credit card"
    }

//    scala.io.Source.stdin.getLines().foreach(line => println("chatbot says: "+chatbot(line)))
    scala.io.Source.stdin.getLines().map(chatbot).foreach(println)

}
