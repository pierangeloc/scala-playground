package com.pierangeloc.scalaz

/**
  * Created by pierangeloc on 2/23/16.
  */

import scala.collection.mutable
import scala.util.Random
import scalaz._
import Scalaz._

object ScalazExamples extends App {

  /**
    * Memoization
    */

  def expensive(id: String): Int = {println("Expensive!!!"); new Random().nextInt()}
  val fooId = "fooId"

  val cache = mutable.Map[String, Int]()

  //sol 1:
  def getSalary(id: String): Int = {
    cache.getOrElseUpdate(id, expensive(id))
  }

  //sol 2: This won't cache
  def getSalary2(id: String): Int = {
    cache.withDefault(expensive)(id)
  }

  val cachez = Memo.immutableHashMapMemo[String, Int] {
    expensive
  }
  def getSalaryScalaz(id: String) = cachez(id)

  println(getSalary("AAA"))
  println(getSalary("AAA"))
  println(getSalary2("BBB"))
  println(getSalary2("BBB"))

  println(getSalaryScalaz("CCC"))
  println(getSalaryScalaz("CCC"))


  /**
    * Functions composition
    */

  def plus1(i: Int): Int = i + 1
  def squared(i: Int): Int = i * i
  def plus1Squared(i: Int) = squared(plus1(i))
  def plus1SquaredZ(i: Int) = i |> plus1 |> squared

  assert((1 to 100 map (i => plus1Squared(i) - plus1SquaredZ(i))).sum == 0)

  /**
    * Option and default values
    */
  case class Cat(name: String)
  val schrodingerCat = None
  println(schrodingerCat | Cat("Tom"))
  val s = none[String]
  val t = "Jerry".some
  println(s)
  println(t)

  /**
    * Validation
    * It accumulates all the errors in a NEL
    * It combines Validation objects through map2 kinda functions
    */
  //has 3 numbers with 3, 2, 4 digits respectively
  case class SSN(first3: Int, second2: Int, third4: Int)
  case class Version(major: Int, minor: Int)

  object Version {
    def validDigit(digit: Int): Validation[String, Int] = {
      if (digit >= 0)
        digit.success[String]
      else "digit must be >= 0".failure
    }

    def validate(major: Int, minor: Int): Validation[NonEmptyList[String], Version] = {
      val majorValidation: ValidationNel[String, Int] = validDigit(major).toValidationNel
      val minorValidation: ValidationNel[String, Int] = validDigit(minor).toValidationNel
      /**
        * this is an example of an appliceative, where we proceed to the validation of all the components, and accumulate failures. If succeeds, the it uses the
        * function passed it to build the result
        */
      val composedValidation = majorValidation |@| minorValidation
      composedValidation(Version(_, _))
    }
  }

}


