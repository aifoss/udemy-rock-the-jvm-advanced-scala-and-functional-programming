package com.learning.scala.lectures.section06.mastering_the_type_system

/**
 * Created by sofia on 2019-12-21.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _52_ReflectionPart1 extends App {

    // reflection + macros + quasi-quotes

    case class Person(name: String) {
        def sayMyName(): Unit = println(s"Hi, my name is $name")
    }


    /* Use Case 1 */

    // step 0: import
    import scala.reflect.runtime.{universe => ru}

    // step 1: instantiate a mirror
    val m = ru.runtimeMirror(getClass.getClassLoader)

    // step 2: create a class object
    val clazz = m.staticClass("com.learning.scala.lectures.section05.mastering_the_type_system._52_ReflectionPart1.Person")

    // step 3: create a reflected mirror
    val cm = m.reflectClass(clazz)

    // step 4: get the constructor
    val constructor = clazz.primaryConstructor.asMethod

    // step 5: reflect the constructor
    val constructorMirror = cm.reflectConstructor(constructor)

    // step 6: invoke the constructor
    val instance = constructorMirror.apply("John")

    println(instance)


    /* Use Case 2 */

    val p = Person("Mary")
    val methodName = "sayMyName"

    // step 2: reflect the instance
    val reflected = m.reflect(p)

    // step 3: create a method symbol
    val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod

    // step 4: reflect the method
    val method = reflected.reflectMethod(methodSymbol)

    // step 5: invoked the method
    method.apply()

}
