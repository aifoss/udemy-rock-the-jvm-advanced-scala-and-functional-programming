package com.learning.scala.lectures.section05.implicits

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _38_TypeClassesPart3 extends App {

    trait HTMLSerializer[T] {
        def serialize(value: T): String
    }

    case class User(name: String, age: Int, email: String)

    implicit object UserSerializer extends HTMLSerializer[User] {
        override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/></div>"
    }

    implicit object IntSerializer extends HTMLSerializer[Int] {
        override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
    }

    implicit class HTMLEnrichment[T](value: T) {
        def toHtml(implicit serializer: HTMLSerializer[T]): String = {
            serializer.serialize(value)
        }
    }

    val john = User("John", 32, "john@rockthejvm.com")

    println(john.toHtml(UserSerializer))
    println(john.toHtml)

    println(3.toHtml)


    /*
        3 components for enriching type:
        - type class itself -- HTMLSerializer[T]
        - type class instances -- UserSerializer, IntSerializer
        - conversion with implicit classes -- HTMLEnrichment[T]
     */


    /*
        Exercise:
        Improve Equality type class with an implicit conversion class:
        ===(another value: T)
        !==(another value: T)
     */

    trait Equality[T] {
        def apply(a: T, b: T): Boolean
    }

    object Equality {
        def apply[T](a: T, b: T)(implicit equalizer: Equality[T]): Boolean = {
            equalizer.apply(a, b)
        }
    }

    implicit object NameEquality extends Equality[User] {
        override def apply(a: User, b: User): Boolean = a.name == b.name
    }

    implicit class TypeSafeEquality[T](value: T) {
        def ===(other: T)(implicit equalizer: Equality[T]): Boolean = {
            equalizer.apply(value, other)
        }

        def !==(other: T)(implicit equalizer: Equality[T]): Boolean = {
            !equalizer.apply(value, other)
        }
    }

    val john2 = User("John", 40, "john@rockthejvm.com")

    println(john === john2)
    /*
        john.===(john2)
        new TypeSafeEquality[User](john).===(john2)
        new TypeSafeEquality[User](john).===(john2)(NameEquality)
     */


    /* implicitly */

    def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
        s"<html><body>${content.toHtml(serializer)}</body></html>"

    def htmlSugar[T: HTMLSerializer](content: T): String = {
        val serializer = implicitly[HTMLSerializer[T]]
        s"<html><body>${content.toHtml(serializer)}</body></html>"
    }

    case class Permissions(mask: String)
    implicit val defaultPermissions: Permissions = Permissions("0744")

    val standardPerms = implicitly[Permissions]

}
