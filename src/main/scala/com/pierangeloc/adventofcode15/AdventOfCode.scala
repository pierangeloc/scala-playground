package com.pierangeloc.adventofcode15

import java.security.MessageDigest

import scala.annotation.tailrec
import scala.collection.immutable.IndexedSeq
import scala.io.Source

/**
 * Created by pierangeloc on 16-12-15.
 */
object AdventOfCode extends App {

  /** Ex 1 **/
  val parens = """()()(()()()(()()((()((()))((()((((()()((((()))()((((())(((((((()(((((((((()(((())(()()(()((()()(()(())(()((((()((()()()((((())((((((()(()(((()())(()((((()))())(())(()(()()))))))))((((((((((((()())()())())(())))(((()()()((((()(((()(()(()()(()(()()(()(((((((())(())(())())))((()())()((((()()((()))(((()()()())))(())))((((())(((()())(())(()))(()((((()())))())((()(())(((()((((()((()(())())))((()))()()(()(()))))((((((((()())((((()()((((()(()())(((((()(()())()))())(((()))()(()(()(()((((()(())(()))(((((()()(()()()(()(((())())(((()()(()()))(((()()(((())())(()(())())()()(())()()()((()(((()(())((()()((())()))((()()))((()()())((((()(()()(()(((()))()(()))))((()(((()()()))(()(((())()(()((()())(()(()()(()())(())()(((()(()())()((((()((()))))())()))((()()()()(())()())()()()((((()))))(()(((()()(((((((())()))()((((()((())()(()())(())()))(()(()())(((((((())))(((()))())))))()))())((())(()()((())()())()))))()((()()())(())((())((((()())())()()()(((()))())))()()))())(()()()(()((((((()()))())()))()(((()(((())((((()()()(()))())()()))))())()))())((())()())(((((())())((())())))(((())(((())(((((()(((((())(()(()())())(()(())(()))(()((((()))())()))))())))((()(()))))())))(((((())()))())()))))()))))(((()))()))))((()))((()((()(()(())()())))(()()()(())()))()((((())))))))(())(()((()()))(()))(()))(()((()))))))()()((((()()))()())()))))))()()()))(()((())(()))((()()()())()(((()((((())())))()((((()(()))))))())))()()())()))(()))))(()())()))))))((())))))))())()))()((())())))(()((()))()))(())))))(()))()())()()))((()(()))()()()()))))())()()))())(())()()))()))((()))))()()(()())))))()()()))((((()))()))))(()(())))(()())))((())())(()))()))))()())))()())()())))))))))()()))))())))((())((()))))())))(((()())))))))(()))()()))(()))()))))()())))))())((((()())))))))())))()()))))))))()))()))))()))))))(())))))))))())))))))))))))))())())((())))))))))()))((())))()))))))))())()(()))))))())))))()()()())()(()()()(()())(()))()()()(()())))())())))()))))())))))))()()()()())(())())()())()))))(()()()()()))))()))())())))((()())()())))()))()))))(()())))()))))))))(((()))()()))))))))))))))))))))(()))(()((()))())))())(()))(()(()(())))))()(()))()))()()))))))))))))()((()())(())())()(())))))())()())((()()))))(()()))))())()(())()))))))))))))))))))))()))(()(()())))))))()()((()))()))))))((())))()))))))))((()))())()()))())()()))((()))())))))))))))(()())()))(())((()(()()))(()())(())))()())(()(())()()))))()))()(()))))))(()))))))))))(()))())))))))))())))))())))(())))))()))))(())())))))))))()(()))))()())))())(()))()())))))))))))))())()()))))()))))))())))))()))))(())(()()()()((())()))())(()))((())()))())())(())(()()))))()))(())()()((())(())))(())))()))())))))))))()(((((())())))(())()))))(())))((()))()(((((((()))))()()))(())))))()(()))))(()()))()))())))))))(()())()))))))))())))(()))())()))(())()((())())()())())(()(()))))()))))))((()())(())()()(()())))()()))(())(())(()))())))()))(()))()()))((((()))))()))((()()()))))()))()))())))(()))()))))(())))()))())()(()))()())))())))))))())))())))()()))))))(()))())())))()))()()())())))))))))))))())))()))(()()))))())))())()(())))())))))))))))))))))()()())())))))()()()((()(()))()()(())()())()))()))))()()()))))))((()))))))))()(()(()((((((()()((()())))))))))))()))())))))((())())(()))())))())))))())()()())(())))())))()())())(())))))))()()(())))()))())))())())())()))))))))()))(()()()())())())))(())())))))))()()())()))))())))())()(())())))))))()())()))(()()(())())))()(()((()()((()()(((((())(()())()))(())()))(())))(())))))))()))()))((()))()))()))))))))()))))))))((()()())(()))(((()))(())))()))((())(((())))()())))())))))((())))))(())())((((((())())()(()))()(()((()())))((())()(()(()))))(())(()()())(())))())((()(((())())))(((()())())))())()(())())((((()()))))())((()))()()()()(())(((((((()()()((()))())(()())))(())())((((()()(()))))()((())))((())()))()(((()))())))()))((()(()))(())(()((((())((((()()(()()))(((())(()))))((((()(()))(())))))((()))(()))((()(((()(()))(()(()((()(())(()(()(()(()()((()))())(((())(()(()))))(()))()()))(())))(())()(((())(()))()((((()()))))())(()))))((())()((((()(((()))())())(((()))()())((())(())())(())()(())()(()()((((((()()))))()()(((()()))))()())()(((()(()))(()(()())(()(()))))(((((()(((())())))))(((((()((()()((())())((((((()(())(()()((()()()()()()()(()()))()(((()))()))(((((((())(((()((()())()((((())(((()(())))()((()(()()()((())((()())()))()))())))())((((((()))(()(()()()))(()((()(()(()))()((()(((()()()((())(((((())()(()))())())((()(())))(()(()())(())((())())())(((()()()(())))))())(()))))))()))))))())((()()()))((()((((((()))(((()((((()()()(((()))())()(()()(((()((()()()()())()()))()()()(()(())((()))))(()))())))))))()(()()(((((())()(()(((((()((()(()()())(()((((((((()((((((())()((((()()()((()((()((((((()))((())))))))())()))((()(()))()(()()(()((())((()()((((((((((((()())(()()()))((((()((((((())(()))())(()()((()()))()(((((((()((()()((((((()(((())))((())))((((((((()()(((((((())(((((()())(((())((())()((((()(((((((()(()(((()((((((()(((()(((((((((((()()((()()(()))((()()(((()(((())))((((())()(()(((())()(()(((())(((((((((((()))())))((((((())((()()((((()())())((((()()))((())(((((()(()()(()()()((())(()((()()((((()(((((()((()(()((((()())((((((()(((((()()(()(()((((())))(())(())(())((((()(()()((((()((((()()((()((((((())))(((((()))))()))(()((((((((()(((())())(((())))(()(()((())(((()((()()(((((()((()()(((())()(()))(((((((())(()(((((()))((()((()((()))(())())((((()((((())()(()))(((()(((((((((((((((())(((((((((()))(((()(()()()()((((((()((())()((((((((()(())(((((((((((()(()((())()((()()(()(()()((((()()((())(()((()()(()()((((()(((((((())))((((())(())()(((()()((()()((((()((()(((()((())(((()()()((((()((((()()(()(()((((((((())(()(((((())(()())(((((((()())()(()((((()((())(()()())((((()()(((()((((())(())(()()(((((((((()()))()(((())(()(()((((((())(()()())(()))()()(((()(((()((())(()(((((((()(()(()((()(((((()(()((()(()((((((()((((()()((((()(((()((())(()(()((()()((((()()(())()(())(((())(()((((((((()())(((((((((()(())()((((())))()))()()(((((()()((((((())(()()(((()(()(((((((()(()(((((((())(())((((()((()(())))((((()()())(()))((()())((((()(((((()(()(())(()(()()())(((((()(((((()((((()()((((((((()()))(()((((((())((((())()(()(((()()()(((()(()(())(())(((((()(())())((((())(())(()(((()(((((())((((())())((()(((((((()(((())(()(()))(((((((((()((()((()()(()((((())(((()((())((((())(()(((()(((()(()((((()(((())(()(((()(()()(()(()((()()(()())(())())((()(()(((()(((()(((()()(((((((((()(((((((((()()(((()(((()())((((()(()(((()()()((())((((((((((())(()(((()((((()())((((()((()))(((()()()(((((()(((((((())((()())(()((((())((((((((())(()((()((((((((((()()((()((()()))(((()())()())()(((()())()()(()(()(((((((())()))(())()))())()()((())()((()((((()((()((())(((((()((((((()(())))(()))())(((()))((()()(()(((()))((((())()(((()))))()(()(())()(((((())(()(()(())(())()((()()()((((()(())((()())(()(()))(()(()(()()(())()()(()((())()((()))))()))((()(()()()()((()())(()))())()(()(((((((((())())((()((()((((((())()((((())(((())((()(()()()((())(()((())(((()((((()()((()(()(((((())()))()((((((()))((())(((()()))(((())(())()))(((((((())(())())()(())(((((()))()((()))()(()()((()()()()()())((((((("""

  def getIndexForFloor(parentheses: String, floor: Int) = {
    parentheses.toCharArray.toStream.scanLeft(0) {
      case (n, '(') => n + 1
      case (n, ')') => n - 1
      case (n, _) => n
    }.zip(Stream.from(0)).filter { case (n, ix) => n == floor }.head
  }

  println("Ex 1: " + getIndexForFloor(parens, -1))


  /** Ex 2 **/
  case class Box(width: Int, height: Int, length: Int)
  val BoxDescription = """(\d+)x(\d+)x(\d+)""".r
  def boxFromString(s: String ) = s match {
    case BoxDescription(w, h, l) => Box(w.toInt, h.toInt, l.toInt)
  }

  def paperForBox(b: Box) = {
    2 * b.length * b.width + 2 * b.width * b.height + 2 * b.height * b.length + List(b.height * b.width, b.width * b.length, b.length * b.height).min
  }

  def ribbonForBox(b: Box) = {
    2 * ((b.length + b.width) min (b.width + b.height) min (b.length + b.height)) + b.length * b.width * b.height
  }


  println("Ex 2, needed paper: " + Source.fromFile("/home/pierangeloc/Documents/projects/scala/reactive-earthquakes/src/main/resources/ex2.txt")
    .getLines().map(boxFromString).foldLeft(0)((n: Int, b: Box) => n + paperForBox(b)))
  println("Ex 2, needed ribbon: " + Source.fromFile("/home/pierangeloc/Documents/projects/scala/reactive-earthquakes/src/main/resources/ex2.txt")
    .getLines().map(boxFromString).foldLeft(0)((n: Int, b: Box) => n + ribbonForBox(b)))

  /** Ex 3 **/
  case class House(x: Int, y: Int)
  val houses = Set(House(0,0))
  def move(from: House, move: Char): House = move match {
    case '<' => House(from.x - 1, from.y)
    case '>' => House(from.x + 1, from.y)
    case '^' => House(from.x, from.y + 1)
    case 'v' => House(from.x, from.y - 1)
  }

  def visitedHousesForMoves(moves: String): Set[House] = {
    moves.toList
      .foldLeft((House(0,0), houses)) {
        case ((house, currentHousesSet), moveChar) => {
          val newHouse = move(house, moveChar)
          (newHouse, currentHousesSet + newHouse)
        }
      }._2
  }

  val movesInTheFile: String = Source.fromFile("/home/pierangeloc/Documents/projects/scala/reactive-earthquakes/src/main/resources/ex3.txt").getLines().next()
  val visitedHousesBySanta = visitedHousesForMoves(movesInTheFile)
  println(s"Ex 3: visited ${visitedHousesBySanta.size} houses")

  def splitEvenOdd[A](l: List[A]): (List[A], List[A]) = {
    val withIndex = l.zipWithIndex
    val evens: List[A] = withIndex.collect {
      case (value, index) if index % 2 == 0 =>  value
    }
    val odds: List[A] =  withIndex.collect {
      case (value, index) if index % 2 != 0 => value
    }
    (evens, odds)
  }

  val (santa, robot) = splitEvenOdd(movesInTheFile.toList)
  val housesVisistedBySantaAndRobot = visitedHousesForMoves(santa.mkString) ++ visitedHousesForMoves(robot.mkString)
  println(s"Ex3: visited by santa and robot: " + housesVisistedBySantaAndRobot.size)


  /** Ex 4 **/
  object MyMD5 {
    val digest = MessageDigest.getInstance("MD5")
    def hash(text: String) = digest.digest(text.getBytes).map("%02x".format(_)).mkString
  }
  val secretKey = "iwrupvqb"
  def numberCracksKey(number: Int, key: String) = MyMD5.hash(key + number).startsWith("000000")
  println("Ex4: cracking number: suspended")// + Stream.from(0).filter(n => numberCracksKey(n, secretKey)).head)

  /** Ex 5 **/
  val prohibited = List("ab", "cd", "pq", "xy")
  val vowels = "aeiou".toList
  def stringNice(s: String) = {
    val containsProhibited = prohibited.map(s.contains(_)).reduce(_ || _)
    val containsThreeVowels = s.map { (c) => if (vowels.contains(c)) 1 else 0 }.sum >= 3

    @tailrec
    def containsAdjacentElements[A](l: List[A]): Boolean = l match {
      case Nil => false
      case a0 :: a1 :: as => if (a0 == a1) true else containsAdjacentElements(a1 :: as)
      case _ => false
    }

    containsThreeVowels && containsAdjacentElements(s.toList) && !containsProhibited
  }

  def stringNicer(s: String) = {
    def contains2LettersWithoutOverlapping(s: String) = {

      def isListSatisfiying[A](l: List[A]): Boolean = {
        l match {
          case x :: y :: tail => if (tail.indexOfSlice(x :: y :: Nil) != -1) true else  isListSatisfiying(y :: tail)
          case _ => false
        }
      }
      isListSatisfiying(s.toList)
    }

    def oneLetterWithAnotherInBetween(s: String) = {
      def isListSatisfying[A](l: List[A]): Boolean = {
        l match {
          case Nil => false
          case x :: y :: z :: tail => if (x == z) true else isListSatisfying(y :: z :: tail)
          case _ => false
        }
      }
      isListSatisfying(s.toList)
    }

    contains2LettersWithoutOverlapping(s) && oneLetterWithAnotherInBetween(s)
  }

  val wordsToCheck: Iterator[String] = Source.fromFile("/home/pierangeloc/Documents/projects/scala/reactive-earthquakes/src/main/resources/ex5.txt").getLines()
  val (it1, it2) = wordsToCheck.duplicate
  println("Ex 5")
  println("number of nice words / p1: " + it1.count(stringNice))
  println("number of nice words / p2: " + it2.count(stringNicer))

  /**
   * Ex 6
   */
  case class Position(x: Int, y: Int)

  sealed trait Instruction
  case object TurnOn extends Instruction
  case object TurnOff extends Instruction
  case object Toggle extends Instruction

  val InstructionDesc = """(toggle|turn on|turn off) (\d{1,3}),(\d{1,3}) through (\d{1,3}),(\d{1,3})""".r
  def extractInstruction(s: String): (Instruction, Position, Position) = {
    s match {
      case InstructionDesc(ins, fromX, fromY, toX, toY) => {
        val from = Position(fromX.toInt, fromY.toInt)
        val to = Position(toX.toInt, toY.toInt)
        ins match {
          case "toggle" => (Toggle, from, to)
          case "turn on" => (TurnOn, from, to)
          case "turn off" => (TurnOff, from, to)
        }
      }
    }
  }

  def rectangle(from: Position, to: Position): IndexedSeq[Position] = for {
                               x <- from.x to to.x
                               y <- from.y to to.y
  } yield Position(x, y)

  /*holding 1M state is too big. we incrementally include lights as they are targeted by an instruction
   **/
  val lights: Vector[Int] = Vector.fill(1000000)(0)

  def pos2Index(p: Position) = p.x + 1000 * p.y

  var itr: Int = 0
  def operate(l: Vector[Int])(instruction: Instruction, from: Position, to: Position): Vector[Int] = {
    itr = itr + 1
    val rect: IndexedSeq[Int] = rectangle(from, to).map(pos2Index)

    def updateLights(currentLights: Vector[Int], at: Int, instruction: Instruction) = {
      val currentLight = currentLights(at)
      instruction match {
        case TurnOff => currentLights.updated(at, 0 max (currentLight - 1))
        case TurnOn => currentLights.updated(at, currentLight + 1)
        case Toggle => currentLights.updated(at, currentLight + 2)
      }
    }
    rect.foldLeft(l)(updateLights(_, _, instruction))
  }

  val finalConfig: Vector[Int] = Source.fromFile("/home/pierangeloc/Documents/projects/scala/reactive-earthquakes/src/main/resources/ex6.txt").getLines()
                      .map(extractInstruction)
                      .foldLeft(lights)((l, row) => operate(l)(row._1, row._2, row._3))

  println("Ex 6: brightness:" + finalConfig.sum)

  /**
   * Ex 7
   */

  val gate2gate = """(\w+) -> (\w+)""".r
  val and = """(\w+) AND (\w+) -> (\w+)""".r
  val or = """(\w+) OR (\w+) -> (\w+)""".r
  val lshift = """(\w+) LSHIFT (\d+) -> (\w+)""".r
  val rshift = """(\w+) RSHIFT (\d+) -> (\w+)""".r


}