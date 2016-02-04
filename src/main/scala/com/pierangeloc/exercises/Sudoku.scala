package com.pierangeloc.exercises

import scala.collection.immutable.IndexedSeq

object Sudoku {

  //define sudoku for arbitrary alphabets

  class Matrix[A] (rowsSeq: List[List[A]], val boardSize: Int = 9, val boxSize: Int = 3) {

    val rows: Vector[Vector[A]] = rowsSeq.toVector.map(_.toVector)
    def transpose(): Matrix[A] = {

      def transposeAsList(m: List[List[A]]): List[List[A]] = m match {
        case r :: Nil => r.map(List(_))
        case r :: rs => (r, transposeAsList(rs)).zipped.map(_ :: _)
        case mx => mx
      }

      new Matrix(transposeAsList(rowsSeq))
    }

    lazy val cols = transpose().rows

    override def toString = {
      (for {
        row <- rows
      } yield row.mkString(" ")
        ).mkString("\n")
    }

    lazy val ungroup: Vector[A] = rows.flatten
    lazy val withPositionsMap: Map[Pos, A] = withPositions.ungroup.map {case (c, pos) => (pos, c)} .toMap

    private def group[B](l: Seq[B]): Matrix[B] = new Matrix(l.toList.grouped(boxSize).toList)

    lazy val withPositions: Matrix[(A, Pos)] = {
      group(ungroup.zipWithIndex.map { case (a, ix) => (a, Pos(ix / boardSize, ix % boardSize))})
    }

    /**
      * Determine the box on a given position. boxes have position (0,0), (0,1), (0,2), (1,0), (1,1), ... ,(2,2)
      * TODO: memoize
      */
    def box(p: Pos): Matrix[A] = {
      val firstRowIndex = p.i * boxSize
      val firstColIndex = p.j * boxSize

      val cells: IndexedSeq[A] = for {
        row <- 0 until boxSize
        col <- 0 until boxSize
      } yield rows(firstRowIndex + row)(firstColIndex + col)

      group(cells.toList)
    }

    def map[B](f: A => B): Matrix[B] = new Matrix(rows.map(_.map(f).toList).toList)

    lazy val boxes: Map[Pos, Matrix[A]] = withPositions.ungroup.groupBy {case (a, pos) => Pos(pos.i / boxSize, pos.j / boxSize)} map {case (boxPosition, boxElements) => (boxPosition, group(boxElements map {case (elem, p) => elem}))}

    def boxForElement(p: Pos): Matrix[A] = boxes(Pos(p.i / boxSize, p.j / boxSize))

  }

  type Board = Matrix[Char]

  //position, 0-based, of a cell
  case class Pos(i: Int, j: Int)


  /**
    * determine when a sudoku is correct:
    * - no blanks
    * - no duplicates along rows
    * - no duplicates along columns
    * - no duplicates along boxes
    */
   val blank = '0'
   val boardSize = 9
   val cellValues = "123456789".toList

   def complete(b: Board) = !b.ungroup.contains('0') &&
                                b.rows.forall(row => !row.contains(blank) && row.distinct.size == boardSize) &&
                                b.cols.forall(col => !col.contains(blank) && col.distinct.size == boardSize)

  /** Determine if a list contains duplicates**/
  def noDups[A](as: List[A]): Boolean = as match {
    case Nil => true
    case x :: xs => !(xs contains x) && noDups(xs)
  }

  //for every element in the board, map it to a singleton list if it is assigned, otherwise replace it with the list of all possible options for that element, compatible with the board
  def expand(m: Board): Matrix[List[Char]] = {
    def choices(b: Matrix[(Char, Pos)], p: Pos): List[Char] = {
      //horrible code!
      val charAndPos: (Char, Pos) = b.withPositionsMap(p)
      if (! (blank == charAndPos._1)) List(charAndPos._1)
      else {
        val rowVals = b.rows(p.i) filter {case (c, rowPos) => c != blank } map {case (c, _) => c}
        val colVals = b.cols(p.j) filter {case (c, colPos) => c != blank } map {case (c, _) => c}
        val boxVals = b.map {case (c, pos) => c} .boxForElement(p).ungroup filter(_ != blank)

        (cellValues.toSet -- (rowVals.toSet ++ colVals.toSet ++ boxVals.toSet)).toList
      }
    }

    val boardWithPositions: Matrix[(Char, Pos)] = m.withPositions
    boardWithPositions.map {case (c, pos) => choices(boardWithPositions, pos)}
  }


  //given a list of lists returns the list of all possible combinations obtained selecting each element from each list
  def cartProd[A](listOfLists: List[List[A]]): List[List[A]] = listOfLists match {
    case Nil => List(Nil)
    case xs :: xss => for {
      x <- xs
      ys <- cartProd(xss)
    } yield x :: ys
  }

  def fromString(table: String): Board = {
    new Matrix(table.filter(_.isDigit).toList.grouped(9).toList)
  }
  def solve(b: Board): List[Matrix[Char]] = {
    cartProd(expand(b).ungroup.toList)
      .map(_.grouped(boardSize).toList)
      .map(new Matrix(_))
      .filter(complete)
  }

}