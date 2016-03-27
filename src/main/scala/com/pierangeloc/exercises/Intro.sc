import com.pierangeloc.exercises.Booksloader

//import scala.annotation.tailrec
//import scala.util.Random
//
//// 1. val vs var
//val x = 2
//// x = 4 reassignment no go
////function inputs are implicitly val
//def add(x: Int, y: Int): Int = x + y
//def add2 = (x: Int, y: Int) => x + y
//// 2. Expression oriented:
//val i = 3
//val p = if (i > 0) 3 else 4
//val isItalian = true
//val salutation = if (isItalian) "Buongiorno" else "Good morning"
//def mod(x: Int) = if (x > 0) x else -x
//
//def errorMsg(code: Int) = code match {
//  case 500 => "Internal Server Error"
//  case 400 => "Bad resource"
//  case x if x >= 400 && x < 499 => "Client Error"
//}
//errorMsg(400)
//errorMsg(401)
////3. Recursion
//def sum(n: Int): Int = if (n == 0) n else n + sum(n -1)
//sum(100)
//
//def sumTR(n: Int): Int = {
//  @tailrec
//  def go(m: Int, acc: Int): Int = {
//    if (m == 0) acc else go(m - 1, acc + m)
//  }
//  go(n, 0)
//}
//
////4. Sum squares, sum cubes, currying
//def square(x: Int) = x * x
//def cube(x: Int) = x * x * x
//def sumSquares(a: Int, b: Int): Int = {
//  if (a == b) square(a)
//  else square(a) + sumSquares(a + 1, b)
//}
//def sumCubes(a: Int, b: Int): Int = {
//  if (a == b) cube(a)
//  else cube(b) + sumCubes(a + 1, b)
//}
//def sumFn(a: Int, b: Int, f: Int => Int): Int = {
//  if (a == b) f(a)
//  else f(a) + sumFn(a + 1, b, f)
//}
//def sumSquares2(a: Int, b: Int) = sumFn(a, b, square)
//def sumCubes2(a: Int, b: Int) = sumFn(a, b, cube)
//sumSquares(1, 19)
//sumSquares2(1, 19)
//def linear(x: Int) = 4 * x + 10
//def sumFnCurried(a: Int, b: Int)(f: Int => Int) = f(a) + f(b)
//def sumZeroToHundred: ((Int) => Int) => Int = sumFnCurried(0, 100)
//sumZeroToHundred(x => x * x + x + 2)
//
////5. Collectionsd
//val numbers = List(1,2,3,4,5,6,7,8,9,10)
//numbers.filter(_ % 2 != 0)
//numbers.partition(_ % 2 == 0)
//numbers.map(_ * 2)
//numbers.reduce(_ + _)
//numbers.foldRight(0)(_ - _)
//numbers.foldLeft(0)(_ - _)
//numbers.foldLeft(List[Int]())((l: List[Int], nr: Int) => nr :: l)
//
//numbers.forall(_ < 100)
//numbers.exists(_ % 4 == 0)
//
//def add3 = (x: Int, y: Int) => x + y
//def mul2(x: Int, y: Int): Int = x * y
//
//val randomVals = List.fill(100)(Random.nextInt)
//randomVals.filter(_ > 0)
//
//List(1,2,3,4,5)(

//val l = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
//
////5. Pattern Matching
//def addThree(l: List[Int]) = l  match {
//  case x0 :: x1 :: x2 :: _ => x0 + x1 + x2
//  case _ => 0
//}
//
//def length[A](l: List[A]): Int = l match {
//  case Nil => 0
//  case h :: t => 1 + length(t)
//}
//
//length(List(1,2,3,4))
//
////6. Case Classes and pattern matching
//case class Celebrity(name: String, surname: String, field: String, age: Int = 100)
//val  people = List(Celebrity("Richard", "Feynman", "physics"),
//  Celebrity("Jimi", "Hendrix", "music"),
//  Celebrity("L.", "Beethoven", "music"),
//  Celebrity("J.C.", "Maxwell", "physics"),
//  Celebrity("E.", "Dijkstra", "CS"))
//
//people.map {
//    case Celebrity(_, _, field, _) => field
//}
//
//people.filter {
//  case Celebrity(_,_,"physics", _) => true
//  case _ => false
//}
//
//sealed trait Shape
//case class Circle(r: Double)
//case class Rect(a: Double, b: Double)
//
//def area(s: Shape) = s match {
//  case Circle(r) => r * r * 3.14
//  case Rect(a, b) => a * b
//}
//

//7. Options
//why exceptions break RT
def doSomething(x: Int): Int = {
  val y: Int = throw new Exception("Boom!")
  try{
    x + y
  } catch {
    case e: Exception => -1
  }
}
//doSomething(42)
def doSomethingSubst(x: Int): Int = {
  try{
    x + ((throw new Exception("Boom!")): Int)
  } catch {
    case e: Exception => -1
  }
}
doSomethingSubst(42)
val nrs = (1 to 100).map(_.toDouble)

def avg(l: Seq[Double]): Option[Double] = {
  if (l.isEmpty) None
  else Some(l.sum / l.length)
}

def variance(l: Seq[Double]): Option[Double] = {
  avg(l).flatMap(m => avg(l.map(x => Math.pow(x - m, 2))))
}
//use cases:
val m: Map[Char, Int] = Map('a' -> 1, 'b' -> 2, 'c' -> 3)
m('a')
m('b')
m.isDefinedAt('z')
m.get('z') //Option
avg(nrs)
variance(nrs)
val books = Booksloader.load()
//
//def firstBookWithRating(topic: String) = {
//  books.groupBy(_.topic).get(topic)
//    .flatMap(books =>
//      books.headOption
//        .flatMap(book => Booksloader.ratings(book.id)
//          .map(rating => s"${book.title} from ${book.authors} has rating: $rating")
//        )
//    )
//}

def firstBookWithRatingReadable(topic: String) = {
  for {
    books <- books.groupBy(_.topic).get(topic)
    firstBook <- books
    rating <- Booksloader.ratings(firstBook.id)
  } yield s"${firstBook.title} from ${firstBook.authors} has rating: $rating"
}

//firstBookWithRating("C"



//,m
//example of lookups
//
//val ints = (1 to 10).toList
//def sum(nrs: List[Int]): Int = {
//  var res = 0
//  for {
//    i <- nrs
//  } res = res + i
//  res
//}
//sum(ints)
//def sumFunctional(nrs: List[Int]) = nrs.foldLeft(0)(_ + _)
//def product(nrs: List[Int]) = nrs.foldLeft(1)(_ * _)
//sumFunctional(ints)
//product(ints)
//def factorial(n: Int): Int = {
//p  else n * factorial(n - 1)
//}
//factorial(3)
