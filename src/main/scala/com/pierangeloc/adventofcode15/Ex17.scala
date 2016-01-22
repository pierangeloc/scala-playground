package com.pierangeloc.adventofcode15

import scala.compat.Platform
import scala.io.Source

/**
  * Created by pierangeloc on 28-12-15.
  */
object Ex17 extends App {

  val tgt = 150
  val containers = Source.fromFile("/home/pierangeloc/Documents/projects/scala/scala-playground/src/main/resources/ex17.txt")
    .getLines().map(_.toInt)
    .toList

  def combinations[A] (l : List[A]): List[List[A]] = {
    l match {
      case x :: xs => {
        val combs = combinations(xs)
        combs.map(x :: _) ++ combs
      }
      case Nil => List(Nil)
    }
  }


  val time = Platform.currentTime
  val allCombinations: List[List[Int]] = combinations(containers)
  println(s"calculating all combinations took ${Platform.currentTime - time} ms")
  println("valid combinations: " + allCombinations.count(_.sum == tgt))
  println(s"it took ${Platform.currentTime - time} ms")

  //part 2
  val minimumNrOfContainers = allCombinations.filter(_.sum == tgt).map(_.size).min
  val combinationsWithMinNrOfContainers = allCombinations.filter(_.sum == tgt).count(_.size == minimumNrOfContainers)
  println(s"Combinations with $minimumNrOfContainers: $combinationsWithMinNrOfContainers")
}
