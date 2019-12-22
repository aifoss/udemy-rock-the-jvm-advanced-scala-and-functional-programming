package com.learning.scala.lectures.section05.implicits

import java.util.Date

/**
 * Created by sofia on 2019-10-26.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _39_TypeClassEndToEndExample_JSONSerialization extends App {

    case class User(name: String, age: Int, email: String)
    case class Post(content: String, createdAt: Date)
    case class Feed(user: User, posts: List[Post])

    /*
        1. intermediate data types: Int, String, List, Date
        2. type classes and instances for conversion to intermediate data types
        3. serialize to JSON
     */

    // intermediate data types

    sealed trait JSONValue {
        def stringify: String
    }

    final case class JSONString(value: String) extends JSONValue {
        override def stringify: String = "\"" + value + "\""
    }

    final case class JSONNumber(value: Int) extends JSONValue {
        override def stringify: String = value.toString
    }

    final case class JSONArray(values: List[JSONValue]) extends JSONValue {
        override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
    }

    final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
        override def stringify: String = values.map {
            case (key, value) => "\"" + key + "\":" + value.stringify
        }.mkString("{", ",", "}")
    }

    val data = JSONObject(Map(
        "user" -> JSONString("John"),
        "posts" -> JSONArray(List(
            JSONString("Scala Rocks!"),
            JSONNumber(453)
        ))
    ))

    println(data.stringify)

    // type class

    trait JSONConverter[T] {
        def convert(value: T): JSONValue
    }

    implicit class JSONOps[T](value: T) {
        def toJSON(implicit converter: JSONConverter[T]): JSONValue =
            converter.convert(value)
    }

    implicit object StringConverter extends JSONConverter[String] {
        override def convert(value: String): JSONValue = JSONString(value)
    }

    implicit object NumberConverter extends JSONConverter[Int] {
        override def convert(value: Int): JSONValue = JSONNumber(value)
    }

    implicit object UserConverter extends JSONConverter[User] {
        override def convert(user: User): JSONValue = JSONObject(Map(
            "name" -> JSONString(user.name),
            "age" -> JSONNumber(user.age),
            "email" -> JSONString(user.email)
        ))
    }

    implicit object PostConverter extends JSONConverter[Post] {
        override def convert(post: Post): JSONValue = JSONObject(Map(
            "content" -> JSONString(post.content),
            "created" -> JSONString(post.createdAt.toString)
        ))
    }

    implicit object FeedConverter extends JSONConverter[Feed] {
        override def convert(feed: Feed): JSONValue = JSONObject(Map(
//            "user" -> UserConverter.convert(feed.user),
//            "posts" -> JSONArray(feed.posts.map(PostConverter.convert))
            "user" -> feed.user.toJSON,
            "posts" -> JSONArray(feed.posts.map(_.toJSON))
        ))
    }

    // call stringify on result

    val now = new Date(System.currentTimeMillis())
    val john = User("John", 34, "john@rockthejvm.com")
    val feed = Feed(john, List(
        Post("hello", now),
        Post("look at this cute puppy", now)
    ))

    println(feed.toJSON.stringify)

}
