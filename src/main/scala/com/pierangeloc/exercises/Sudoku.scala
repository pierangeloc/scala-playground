package com.pierangeloc.exercises

import scala.collection.immutable.IndexedSeq

object Sudoku extends App {

  //define sudoku for arbitrary alphabets
  type Matrix[A] = List[List[A]]
  type Board = Matrix[String]

  val boardSize = 9
  val boxSize = 3
  val cellValues = "123456789".toList
  def blank(c: Char) = c == '.'

  val example: Board = (for {
    r <- 1 to boardSize
    c <- 1 to boardSize
  } yield (r * 10 + c).toString).toList.grouped(boardSize).toList

  def asString[A](m: Matrix[A]): String = {
    (for {
      row <- m
    } yield row.mkString(" ")
    ).mkString("\n")
  }


  def rows[A](b: Matrix[A]): Matrix[A] = b

  def transpose[A](m: Matrix[A]): Matrix[A] = m match {
    case row :: Nil => row.map(List(_))
    case row :: rows => (row, transpose(rows)).zipped.map(_ :: _)
    case mx => mx
  }

  def cols[A](b: Matrix[A]) = transpose(b)

  def ungroup[A](b: Matrix[A]): List[A] = b.flatten
  def group[A](l: List[A]): Matrix[A] = l.grouped(boxSize).toList

  def box[A](b: Matrix[A])(i: Int, j: Int): Matrix[A] = {
    val firstRowIndex = i * boxSize
    val firstColIndex = j * boxSize

    val cells: IndexedSeq[A] = for {
      row <- 0 until boxSize
      col <- 0 until boxSize
    } yield b(firstRowIndex + row)(firstColIndex + col)

    group(cells.toList)
  }

  def boxes[A](b: Matrix[A]): List[Matrix[A]] = {
    (for {
      i <- 0 until boxSize
      j <- 0 until boxSize
    } yield box(b)(i, j)
      ).toList
  }


  println("sudoku simple: " + example)
  println("formatted:\n"  + asString(example))

  /**
    * determine when a sudoku is correct:
    * - no blanks
    * - no duplicates along rows
    * - no duplicates along columns
    * - no duplicates along boxes
    */
   def complete(b: Board) = ???

  /** Determine if a list contains duplicates**/
  def noDups[A](as: List[A]): Boolean = as match {
    case Nil => true
    case x :: xs => !(xs contains x) && noDups(xs)
  }




}
