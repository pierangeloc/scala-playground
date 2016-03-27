package com.pierangeloc.exercises

import scala.collection.immutable.IndexedSeq
import scala.io.Source
import scala.util.Random
import scala.util.matching.Regex

case class Book(id: Int, title: String, authors: List[String], topic: String)

object Booksloader {

  val BookRow: Regex = "([^\\|].*)\\|([^\\|]*)\\|([^\\|]*)".r

  def load(path: String = "/home/pierangeloc/Documents/projects/scala/scala-playground/src/main/resources/programming-books.txt"): List[Book] =
    Source.fromFile(path)
      .getLines().map{
    case BookRow(title, authors, topic) => (title, authors.split(",").toList, topic)
  }.toList.zipWithIndex.map{
    case ((title, author, topic), index) => Book(index + 1, title, author, topic)
  }

  val ratingsPath = "/home/pierangeloc/Documents/projects/scala/scala-playground/src/main/resources/programming-books-ratings.txt"
  val RatingRow = "([^\\|].*)\\|([^\\|]*)".r
  lazy val ratings: Map[Int, Int] =
    Source.fromFile(ratingsPath)
        .getLines().map {
      case RatingRow(id, rating) => (id.toInt, rating.toInt)
    }.toMap
}
