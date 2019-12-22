package com.learning.scala.lectures.section05.implicits

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _35_TypeClassesPart1 extends App {

    /*
        Disadvantages:
        1. works only for the types we write
        2. one implementation out of quite a number
     */

    trait HTMLWritable {
        def toHtml: String
    }

    case class User(name: String, age: Int, email: String) extends HTMLWritable {
        override def toHtml: String = s"<div>$name ($age yo) <a href=$email/></div>"
    }

    val john = User("John", 32, "john@rockthejvm.com")

    println(john.toHtml)


    /*
        Disadvantages:
        1. lost type safety
        2. need to modify the code every time
        3. still one implementation for each type
     */

    object HTMLSerializerPM {
        def serializeToHtml(value: Any) = value match {
            case User(n, a, e) =>
            case _ =>
        }
    }

    /*
        Advantages:
        1. we can define serializers for other types
        2. we can define multiple serializers
     */

    // type class
    trait HTMLSerializer[T] {
        def serialize(value: T): String
    }

    // type class instance
    object UserSerializer extends HTMLSerializer[User] {
        override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/></div>"
    }

    import java.util.Date
    object DateSerializer extends HTMLSerializer[Date] {
        override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
    }

    object PartialUserSerializer extends HTMLSerializer[User] {
        override def serialize(user: User): String = s"<div>${user.name}</div>"
    }

    println(UserSerializer.serialize(john))


    /*
        Exercise:
        Implement a type class and type class instances for equality.
     */

    trait Equal[T] {
        def apply(a: T, b: T): Boolean
    }

    object UserNameEquality extends Equal[User] {
        override def apply(a: User, b: User): Boolean = a.name == b.name
    }

    object UserNameEmailEquality extends Equal[User] {
        override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
    }

}
