package com.learning.scala.lectures.section04.functional_concurrent_programming

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

/**
 * Created by sofia on 2019-10-20.
 */

/**
 * Source:
 * Udemy Rock the JVM! Advanced Scala and Functional Programming Course
 */
object _28_FuturesPart2 extends App {

    case class Profile(id: String, name: String) {

        def poke(anotherProfile: Profile): Unit = {
            println(s"${this.name} poking ${anotherProfile.name}")
        }

    }

    object SocialNetwork {

        // "database"
        val names = Map(
            "fb.id.1-zuck" -> "Mark",
            "fb.id.2-bill" -> "Bill",
            "fb.id.0-dummy" -> "Dummy"
        )

        val friends = Map(
            "fb.id.1-zuck" -> "fb.id.2-bill"
        )

        val random = new Random()

        // API
        def fetchProfile(id: String): Future[Profile] = Future {
            Thread.sleep(random.nextInt(300))
            Profile(id, names(id))
        }

        def fetchBestFriend(profile: Profile): Future[Profile] = Future {
            Thread.sleep(random.nextInt(400))
            val bfId = friends(profile.id)
            Profile(bfId, names(bfId))
        }

    }

    // client: mark to poke bill
    val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")

    mark.onComplete {
        case Success(markProfile) => {
            val bill = SocialNetwork.fetchBestFriend(markProfile)

            bill.onComplete {
                case Success(billProfile) => markProfile.poke(billProfile)
                case Failure(e) => e.printStackTrace()
            }
        }
        case Failure(ex) => ex.printStackTrace()
    }

    Thread.sleep(1000)


    // functional composition of futures

    val nameOnTheWall: Future[String] = mark.map(profile => profile.name)

    val marksBestFriend: Future[Profile] = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

    val marksBestFriendRestricted: Future[Profile] = marksBestFriend.filter(profile => profile.name.startsWith("Z"))


    // for-comprehension

    for {
        mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
        bill <- SocialNetwork.fetchBestFriend(mark)
    } mark.poke(bill)

    Thread.sleep(1000)


    // fallbacks

    val aProfileNoMatterWhat: Future[Profile] = SocialNetwork.fetchProfile("unknown id").recover {
        case e: Throwable => Profile("fb.id.0-dummy", "Forever Alone")
    }

    val aFetchedProfileNoMatterWhat: Future[Profile] = SocialNetwork.fetchProfile("unknown id").recoverWith {
        case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
    }

    val fallbackResult: Future[Profile] = SocialNetwork.fetchProfile("unknown id").fallbackTo(
        SocialNetwork.fetchProfile("fb.id.0-dummy")
    )

}
