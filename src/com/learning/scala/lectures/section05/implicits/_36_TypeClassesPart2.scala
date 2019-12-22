package com.learning.scala.lectures.section05.implicits

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _36_TypeClassesPart2 extends App {

    trait HTMLSerializer[T] {
        def serialize(value: T): String
    }

    object HTMLSerializer {
        def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
            serializer.serialize(value)

        def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
    }

    implicit object IntSerializer extends HTMLSerializer[Int] {
        override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
    }

    case class User(name: String, age: Int, email: String)

    implicit object UserSerializer extends HTMLSerializer[User] {
        override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/></div>"
    }

    println(HTMLSerializer.serialize(42))

    val john = User("John", 32, "john@rockthejvm.com")

    println(HTMLSerializer.serialize(john))

    // have access to entire type class interface
    println(HTMLSerializer[Int].serialize(42))
    println(HTMLSerializer[User].serialize(john))


    /*
        Type class Pattern
     */

    trait MyTypeClassTemplate[T] {
        def action(value: T): String
    }

    object MyTypeClassTemplate {
        def apply[T](implicit instance: MyTypeClassTemplate[T]): MyTypeClassTemplate[T] = instance
    }


    /*
        Exercise:
        Implement the type class pattern for equality.
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

    val anotherJohn = User("John", 45, "anotherjohn@rockthejvm.com")

    println(Equality.apply(john, anotherJohn))

}
