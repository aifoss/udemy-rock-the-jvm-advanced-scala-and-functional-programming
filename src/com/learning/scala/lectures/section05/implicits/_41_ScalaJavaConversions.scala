package com.learning.scala.lectures.section05.implicits

import java.{util => ju}

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _41_ScalaJavaConversions extends App {

    import collection.JavaConverters._

    val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
    (1 to 5).foreach(javaSet.add)
    println(javaSet)

    val scalaSet = javaSet.asScala
    println(scalaSet)

    import collection.mutable._

    val numberBuffer = ArrayBuffer[Int](1, 2, 3)
    val juNumberBuffer = numberBuffer.asJava

    println(juNumberBuffer.asScala eq numberBuffer)

    val numberList = List(1, 2, 3)
    val juNumberList = numberList.asJava
    val scalaNumberList = juNumberList.asScala

    println(scalaNumberList eq numberList)
    println(scalaNumberList == numberList)


    /*
        Exercise:
        Create a Scala-Java Option-Option.
     */

    class ToScala[T](value: => T) {
        def asScala: T = value
    }

    implicit def asScalaOptional[T](o: ju.Optional[T]): ToScala[Option[T]] =
        new ToScala[Option[T]](
            if (o.isPresent) Some(o.get) else None
        )

    class ToJava[T](value: => T) {
        def asJava: T = value
    }

    implicit def asJavaOptional[T](o: Option[T]): ToJava[ju.Optional[T]] =
        new ToJava[ju.Optional[T]](
            if (o.isDefined) ju.Optional.of(o.get) else null
        )

    val juOptional: ju.Optional[Int] = ju.Optional.of(2)
    val toScalaOptional = juOptional.asScala
    val fromScalaOptional = toScalaOptional.asJava

    println(toScalaOptional)
    println(fromScalaOptional)

}
