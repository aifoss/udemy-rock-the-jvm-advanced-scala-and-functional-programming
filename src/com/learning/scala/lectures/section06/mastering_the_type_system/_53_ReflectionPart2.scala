package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-12-21.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _53_ReflectionPart2 extends App {

    case class Person(name: String) {
        def sayMyName(): Unit = println(s"Hi, my name is $name")
    }


    // type erasure

    // pain point #1: differentiate types at runtime

    val numbers = List(1,2,3)

    numbers match {
        case listOfStrings: List[String] => println("list of strings")
        case listOfNumbers: List[Int] => println("list of numbers")
    }

    // paint point #2: limitations on overloads

//    def processList(list: List[Int]): Int = 43
//    def processList(list: List[String]): Int = 45


    // type tags

    // step 0: import
    import scala.reflect.runtime.{universe => ru}
    import ru._

    // step 1: create a type tag
    val ttag = typeTag[Person]
    println(ttag.tpe)

    class MyMap[K, V]

    // step 2: pass type tags as implicit parameters
    def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]) = typeTag.tpe match {
        case TypeRef(_, _, typeArguments) => typeArguments
        case _ => List()
    }

    val myMap = new MyMap[Int, String]

    val typeArgs = getTypeArguments(myMap) // (typeTag: TypeTag[MyMap[Int, String]])

    println(typeArgs)

    def isSubtype[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
        ttagA.tpe <:< ttagB.tpe
    }

    class Animal
    class Dog extends Animal

    println(isSubtype[Dog, Animal])


    val p = Person("Mary")
    val methodName = "sayMyName"

    val m = ru.runtimeMirror(getClass.getClassLoader)

    // step 2: reflect the instance
    val reflected = m.reflect(p)

    // step 3: create a method symbol
    val methodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod

    // step 4: reflect the method
    val method = reflected.reflectMethod(methodSymbol)

    // step 5: invoked the method
    method.apply()

}
