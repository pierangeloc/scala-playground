package com.pierangeloc.typelevel

import scala.util.Try

/**
  * See https://meta.plasm.us/posts/2015/11/08/type-classes-and-generic-derivation/
  *
  */
object Typelevel {

  /**
    * Target:
    * We want to be able to parse from String to one of these type classes, without hassle and with compile time
    * awareness, in case it is impossible to extract such a class from such a String
    */
  case class Person(name: String, age: Double)
  case class Book(title: String, author: String, year: Int)
  case class Country(name: String, population: Int, area: Double)

  /**
    * Solution 1: define explicitly a parser for each case class, and invoke it explicitly
    *
    *
    * scala> personParser("Pierangelo,39")
    * res4: com.pierangeloc.typelevel.Typelevel.Person = Person(Pierangelo,39.0)
    **
    * scala> bookParser("Divina Commedia,Dante Alighieri,1320")
    * res5: com.pierangeloc.typelevel.Typelevel.Book = Book(Divina Commedia,Dante Alighieri,1320)
    **
    * scala> bookParser("Divina Commedia,Dante Alighieri,thirteentwenty")
    * java.lang.NumberFormatException: For input string: "thirteentwenty"
    */
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

  /**
    * Solution 2: fail gracefully, return option. And use type classes so we can invoke the parser
    * that can be applied for the target type
    * scala> SafeRowParser[Book]("Divina Commedia,Dante Alighieri,thirteentwenty")
    * res7: Option[com.pierangeloc.typelevel.Typelevel.Book] = None
    **
    * scala> SafeRowParser[Book]("Divina Commedia,Dante Alighieri,1320")
    * res8: Option[com.pierangeloc.typelevel.Typelevel.Book] = Some(Book(Divina Commedia,Dante Alighieri,1320))
    *
    */

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

  /**
    * To demostrate how type classes work, let's just add a new case class with a parser specified in its
    * companion object
    *
    * scala> SafeRowParser[Aircraft]("A380,Airliner,79.75")
    * res3: Option[com.pierangeloc.typelevel.Typelevel.Aircraft] = Some(Aircraft(A380,79.75,79.75))
    *
    */
  case class Aircraft(name: String, kind: String, wingspan: Double)
  object Aircraft {
    implicit val safeAircraftParser: SafeRowParser[Aircraft] = new SafeRowParser[Aircraft] {
      def apply(s: String): Option[Aircraft] = try {
        s.split(",").toList match {
          case List(name, kind, wingspan) => Some(Aircraft(name, wingspan, wingspan.toDouble))
          case _ => None
        }
      }catch {
        case e: Throwable => None
      }
    }
  }

  /**
    * Sol nr 3: Automate everything with Shapeless
    */
  import shapeless._
  trait Parser[A] {
    def apply(s: String): Option[A]
  }

  // Step0: define base parsers, as implicit vals
  implicit val stringParser: Parser[String] = new Parser[String] {
    def apply(s: String) = Some(s)
  }
  implicit val doubleParser: Parser[Double] = new Parser[Double] {
    def apply(s: String) = Try(s.toDouble).toOption
  }
  implicit val intParser: Parser[Int] = new Parser[Int] {
    def apply(s: String) = Try(s.toInt).toOption
  }

  // Step1: define parsers to HList, using the inductive structure of it
  implicit val hnilParser: Parser[HNil] = new Parser[HNil] {
    def apply(s: String) = if(s.isEmpty) Some(HNil) else None
  }
  //read it like: for every Head for which exists a Parser
  //              and
  //              for every Tail descending from HList, for which exists a Parser, we can build a parser for the H :: T
  implicit def hConsParser[H : Parser, T <: HList : Parser]: Parser[H :: T] = new Parser[H :: T] {
    def apply(s: String): Option[H :: T] = s.split(",").toList match {
      case init +: rest => for {
        head <- implicitly[Parser[H]].apply(init)
        tail <- implicitly[Parser[T]].apply(rest.mkString(","))
      } yield head :: tail
      case _ => None
    }
  }

  //Step 2: Derive a parser to the case class instead of to the HList
  //In Shapeless Generic[T] where T is a case class provides methods from(hList): T and to(t: T):HList
  //with the code above we implicitly derive parsers for any HList
  //we want to provide a parser for every type for which Generic[T] provide an Repr(=HList) for which we can derive a parser
  implicit def caseClassParser[A, R <: HList](implicit gen: Generic[A]{type Repr = R}, reprParser: Parser[R]): Parser[A] = new Parser[A] {
    def apply(s: String): Option[A] = reprParser.apply(s).map(gen.from)
  }

  //Step 3: Wrap it all together
  object Parser {
    def apply[A](s: String)(implicit p: Parser[A]): Option[A] = {
      p(s)
    }
  }



}


