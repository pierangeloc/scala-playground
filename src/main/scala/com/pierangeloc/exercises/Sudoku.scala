package com.pierangeloc.exercises

import scala.collection.immutable.IndexedSeq

object Sudoku {//} extends App{

  //define sudoku for arbitrary alphabets

  class Matrix[A] (rowsSeq: List[List[A]], val boardSize: Int = 9, val boxSize: Int = 3) {
    import Matrix._

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
      "\n" + (for {
        row <- rows
      } yield "|" + row.mkString(" ") + "|"
        ).mkString("\n")
    }

    lazy val ungroup: Vector[A] = rows.flatten
    lazy val withPositionsMap: Map[Pos, A] = withPositions.ungroup.toMap

    lazy val withPositions: Matrix[(Pos, A)] = {
      group(ungroup.zipWithIndex.map { case (a, ix) => (Pos(ix / boardSize, ix % boardSize), a)})
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

    lazy val boxes: Map[Pos, Matrix[A]] = withPositions.ungroup
                                              .groupBy {case (pos, a) => Pos(pos.i / boxSize, pos.j / boxSize)}
                                              .map {case (boxPosition, boxElements) => (boxPosition, group(boxElements map {case (p, elem) => elem}))}

    def boxForElement(p: Pos): Matrix[A] = boxes(Pos(p.i / boxSize, p.j / boxSize))

  }

  object Matrix {
    def group[B](l: Seq[B]): Matrix[B] = new Matrix(l.toList.grouped(boardSize).toList)

    //make a matrix out of a matrix of (Pos,A)
    def convert[A](withPositions: List[(Pos, A)]): Matrix[A] = {
      group(withPositions.sortBy {case (pos, c) => (pos.i, pos.j)} .map {case(p, a) => a})
    }
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

   def complete(b: Board) = !b.ungroup.contains(blank) &&
                                b.rows.forall(row => row.distinct.size == boardSize) &&
                                b.cols.forall(col => col.distinct.size == boardSize)


  def choices(b: Matrix[(Pos, Char)], p: Pos): List[Char] = {
    //horrible code!
    val charAndPos: (Pos, Char) = b.withPositionsMap(p)
    if (! (blank == charAndPos._2)) List(charAndPos._2)
    else {
      val rowVals = b.rows(p.i) filter {case (rowPos, c) => c != blank } map {case (_, c) => c}
      val colVals = b.cols(p.j) filter {case (colPos, c) => c != blank } map {case (_, c) => c}
      val boxVals = b.map {case (pos, c) => c} .boxForElement(p).ungroup filter(_ != blank)
      (cellValues.toSet -- (rowVals.toSet ++ colVals.toSet ++ boxVals.toSet)).toList
    }
  }

  //for every element in the board, map it to a singleton list if it is assigned, otherwise replace it with the list of all possible options for that element, compatible with the board
  def expand(m: Board): Matrix[(Pos, List[Char])] = {
    val boardWithPositions: Matrix[(Pos, Char)] = m.withPositions
    boardWithPositions.map {case (pos, c) => (pos, choices(boardWithPositions, pos))}
  }


  //given a list of lists returns the list of all possible combinations obtained selecting each element from each list
  var combNr = 0
  def cartProd[A](listOfLists: List[Stream[A]]): Stream[List[A]] = listOfLists match {
    case Nil => Stream(Nil)
    case xs :: xss => for {
      x <- xs
      ys <- cartProd(xss)
    } yield x :: ys
  }

  def fromString(table: String): Board = {
    new Matrix(table.filter(_.isDigit).toList.grouped(9).toList)
  }
  def solve(b: Board): Matrix[Char] = {

    val listOfStreams: List[Stream[(Pos, Char)]] = expand(b).ungroup.toList.sortBy {case (pos, list) => list.length} .map {case(pos, list) => list.map(c => (pos, c)).toStream}
    val streamOfLists: Stream[List[(Pos, Char)]] = cartProd(listOfStreams)

    streamOfLists.map(Matrix.convert).filter(complete).head
  }

//
//  val table3 = """
//                 |000260701
//                 |680070093
//                 |190004500
//                 |820100040
//                 |004602900
//                 |050003028
//                 |009300074
//                 |040050036
//                 |703018000
//               """.stripMargin
//  println(solve(fromString(table3)))
}