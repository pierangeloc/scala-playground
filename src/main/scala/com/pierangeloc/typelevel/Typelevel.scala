package com.pierangeloc.typelevel

/**
  * Created by pierangeloc on 3/24/16.
  */
object Typelevel {
  case class Person(name: String, age: Double)
  case class Book(title: String, author: String, year: Int)
  case class Country(name: String, population: Int, area: Double)

  trait RowParser[A] {
    def apply(s: String): A
  }

  val personParser: RowParser[Person] = new RowParser[Person] {
    def apply(s: String): Person = s.split(",").toList match {
      case List(name, age) => Person(name, age.toDouble)
    }
  }

  val bookParser: RowParser[Book] = new RowParser[Book] {
    def apply(s: String): Book = s.split(",").toList match {
      case List(title, author, year) => Book(title, author, year.toInt)
    }
  }


  trait SafeRowParser[A] {
    def apply(s: String): Option[A]
  }

  object SafeRowParser {
    def apply[A: SafeRowParser](s: String): Option[A] = {
      val parser = implicitly[SafeRowParser[A]]
      parser(s)
    }

    implicit val safePersonParser: SafeRowParser[Person] = new SafeRowParser[Person] {
      def apply(s: String): Option[Person] = try {
        s.split(",").toList match {
          case List(name, age) => Some(Person(name, age.toDouble))
          case _ => None
        }
      }catch {
        case e: Throwable => None
      }
    }
1
    implicit val safeBookParser: SafeRowParser[Book] = new SafeRowParser[Book] {
      def apply(s: String): Option[Book] = try {
        s.split(",").toList match {
          case List(title, author, year) => Some(Book(title, author, year.toInt))
          case _ => None
        }
      }catch {
        case e: Throwable => None
      }
    }
  }



}


