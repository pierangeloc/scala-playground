import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import scalaz.OptionT._


/**
  * Understandig nested monads and monad transformers: https://www.youtube.com/watch?v=hGMndafDcc8
  */

/**
  * To deal with a Future[Option[A]]
  * @param inner
  * @tparam A
  */
case class FutureOption[A](inner: Future[Option[A]]) {

  def map[B](f: A => B): FutureOption[B] = FutureOption {
    inner.map(_.map(f))
  }

  def flatMap[B](f: A => FutureOption[B]): FutureOption[B] = FutureOption {
    inner.flatMap {
      case Some(a) => f(a).inner
      case None => Future.successful(None)
    }
  }
}


def getX = Future(Some(5))
def getY = Future(Some(3))

val z1: FutureOption[Int] = for {
  x <- FutureOption(getX)
  y <- FutureOption(getY)
} yield x + y

val z = z1.inner


/**
  *   Part2: Generalize FutureOption
  *   Future is a Monad, let's generalize the Future to a generic monad
  */
trait Monad[M[_]] {
  def map[A, B](ma: M[A])(f: A => B): M[B]
  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
  def create[A](a: A): M[A]
}

/**
  * This is a Monad in turn, and it takes any monad M and makes it another monad AnyMonadOption,
  * so it is  a _Monad Transformer_
  * N.B.: we could not make a transformer for any outer and any inner monad, we have to fix one of the 2 monads
  */
case class AnyMonadOption[M[_], A](inner: M[Option[A]])(implicit m: Monad[M]) {

  def map[B](f: A => B): AnyMonadOption[M, B] = AnyMonadOption {
    m.map(inner)(_.map(f))
  }

  def flatMap[B](f: A => AnyMonadOption[M, B]): AnyMonadOption[M, B] = AnyMonadOption {
    m.flatMap(inner) {
      case Some(a) => f(a).inner
      case None => m.create(None)
    }
  }
}

/**
  * Part3: Use scalaz
  * Monad tf are OptionT...
  */
def  getXz: Future[Option[Int]] = Future(Some(5))
def  getYz: Future[Option[Int]] = Future(Some(3))
val zz: OptionT[Future, Int] = for {
  x <- OptionT(getXz)
  y <- OptionT(getYz)
} yield x + y
val zzi = zz.run

/**
  * Part 4: Other combinators
  * We want to inform about error (e.g. validation)
  * Either[L, R] is not a monad
  * ==> Use \/ from Scalaz
 */

def age(name: String): String \/ Int = name match {
  case "Piero" => \/-(39)
  case other => -\/(s"Age of $other is unknown")
}

age("Piero")
age("John")

//fails on Luca, that's the final result
val totalAge = for {
  a <- age("Piero")
  b <- age("Luca")
  c <- age("Marco")
} yield a + b + c

def ageAsync(name: String): Future[String \/ Int] = Future(age(name))

//use EitherTransformer:
val resultEither = for {
  a <- EitherT(ageAsync("Piero"))
  b <- EitherT(ageAsync("Luca"))
  c <- EitherT(ageAsync("Marco"))
} yield a + b + c

Some(5).toRightDisjunction()
None.toRightDisjunction("Absent")
/**
  * Part 5: How about things with different shapes?
  * put them all to same shape
  */
def getA5: Future[String \/ Int] = ???
def getB5: Option[Int] = ???
def getC5: Future[Int] = ???

//pick the richest structure Future[String \/ Int]
val result5: EitherT[Future, String, Int] = for {
  a <- EitherT(getA5)
// \/> takes an option and promotes it to a Right, or left with error message
  b <- EitherT(Future(getB5 \/> "B is missing"))
  c <- EitherT(getC5.map(\/.right))
} yield a + b + c