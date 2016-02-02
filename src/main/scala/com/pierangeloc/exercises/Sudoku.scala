package com.pierangeloc.exercises

import scala.collection.immutable.IndexedSeq

object Sudoku {

  //define sudoku for arbitrary alphabets
  type Matrix[A] = List[List[A]]
  type Board = Matrix[Char]

  //position, 0-based, of a cell
  case class Pos(i: Int, j: Int)

  val boardSize = 9
  val boxSize = 3
  val cellValues = "123456789".toList
  def blank(c: Char) = c == '.'

  val example: Board = (for {
    r <- 1 to boardSize
    c <- 1 to boardSize
  } yield (' ' + r * 10 + c).toChar).toList.grouped(boardSize).toList

  def asString[A](m: Matrix[A]): String = {
    (for {
      row <- m
    } yield row.mkString(" ")
    ).mkString("\n")
  }



  def ungroup[A](b: Matrix[A]): List[A] = b.flatten
  def group[A](l: List[A]): Matrix[A] = l.grouped(boxSize).toList
  def withPositions[A](b: Matrix[A]): Matrix[(A, Pos)] = {
    group(ungroup(b).zipWithIndex.map { case (a, ix) => (a, Pos(ix / boardSize, ix % boardSize))})
  }

  def box[A](b: Matrix[A])(p: Pos): Matrix[A] = {
    val firstRowIndex = p.i * boxSize
    val firstColIndex = p.j * boxSize

    val cells: IndexedSeq[A] = for {
      row <- 0 until boxSize
      col <- 0 until boxSize
    } yield b(firstRowIndex + row)(firstColIndex + col)

    group(cells.toList)
  }

  def boxForElement[A](b: Matrix[A])(p: Pos): Matrix[A] = box(b)(p.i / boxSize, p.j / boxSize)

  def rows[A](b: Matrix[A]): Matrix[A] = b

  def transpose[A](m: Matrix[A]): Matrix[A] = m match {
    case row :: Nil => row.map(List(_))
    case row :: rows => (row, transpose(rows)).zipped.map(_ :: _)
    case mx => mx
  }

  def cols[A](b: Matrix[A]) = transpose(b)

  //boxes are numbered as 00,01,02,10,11,12,20,21,22
  def allBoxes[A](b: Matrix[A]): List[Matrix[A]] = {
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
   val blank = '0'
   def complete(b: Board) = !ungroup(b).contains('0') &&
                                rows(b).forall(row => !row.contains(blank) && row.distinct.size == boardSize) &&
                                cols(b).forall(col => !col.contains(blank) && col.distinct.size == boardSize)

  /** Determine if a list contains duplicates**/
  def noDups[A](as: List[A]): Boolean = as match {
    case Nil => true
    case x :: xs => !(xs contains x) && noDups(xs)
  }

  def expand(m: Board): Matrix[List[Char]] = {
    val withPos: Matrix[(Char, Pos)] = withPositions(m)

    def choices(b: Matrix[(Char, Pos)], p: Pos) = {
      val cell: (Char, Pos) = ungroup(b).find {case (c, pos) => p == pos}.get
      cellValues.diff(rows(b)(p.i).filter {case (c, pos) => !blank(c)}) ++
      cellValues.diff(cols(b)(p.j).filter {case (c, pos) => !blank(c)}) ++
      cellValues.diff(boxForElement(b)(p).filter {case (c, pos) => !blank(c)})
    }

    ???
  }



}
