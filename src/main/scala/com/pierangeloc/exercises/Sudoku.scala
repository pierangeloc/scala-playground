package com.pierangeloc.exercises

object Sudoku extends App {

  //define sudoku for arbitrary alphabets
  type Matrix[A] = List[List[A]]
  type Board = Matrix[String]

  val boardSize = 9
  val boxSize = 3
  val cellValues = "123456789".toList
  def blank(c: Char) = c == '.'

  val example: Board = (for {
    r <- 1 to 9
    c <- 1 to 9
  } yield (r * 10 + c).toString).toList.grouped(boardSize).toList

  def asString[A](m: Matrix[A]) = {
    for {
      row <- m
    } row.mkString(" ")
    .mkString("\n")
  }

  println((example))
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

  def rows[A](b: Matrix[A]): Matrix[A] = b

  def transpose[A](m: Matrix[A]): Matrix[A] = m match {
    case row :: Nil => row.map(List(_))
    case row :: rows => (row, transpose(rows)).zipped.map(_ :: _)
    case mx => mx
  }

  def cols[A](b: Matrix[A]) = transpose(b)

  def ungroup[A](b: Matrix[A]): List[A] = b.flatten
  def group[A](l: List[A]): Matrix[A] = l.grouped(boxSize).toList


  def boxes[A](b: Matrix[A]) = {
    val grouped = group(ungroup(b))
    val res = for {
      i <- 0 to boxSize
      residual <- grouped.drop(i)
    } yield residual
    println(res)
    ???
  }
  println("Pierangelo".updated(5,'K'))



  //test
  val A = List(List(1,2,3),
                List(4,5,6),
                List(7,8,9))
  println(A)
  println(transpose(A))
  println(group(ungroup(A)))
  println(ungroup(A))
  println (group(1 to 81 toList))
}
