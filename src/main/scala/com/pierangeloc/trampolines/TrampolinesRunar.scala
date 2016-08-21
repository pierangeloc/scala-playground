package com.pierangeloc.trampolines

/**
  *
  * My attempts to grasp what a Trampoline is, going through:
  * http://blog.higher-order.com/assets/trampolines.pdf
  */

/**
  * For a given S, State[S, A]  is a monad
  */
case class State[S,+A](runS: S => (A,S)) {

  def map[B](f: A => B) = State[S, B](s => {
      val (a,s1) = runS(s)
      (f(a),s1)
    })

  def flatMap[B](f: A => State[S,B]) = State[S,B](s => {
      val (a,s1) = runS(s)
      f(a) runS s1
    })
}

object State {
  def getState[S]: State[S,S] = State(s => (s,s))
  def setState[S](s: S): State[S, Unit] = State(_ => ((),s))
  def pureState[S, A](a: A): State[S, A] = State(s => (a,s))
}

sealed trait Trampoline[+A] {

  final def runT: A = this.resume match {
    case Right(a) => a
    case Left(cont) => cont().runT
  }

  def flatMap[B](f: A => Trampoline[B]) = More(() => f(runT)) //flawed version, as runT is not in tail position

  /**
    * Helper method to decide what to do with the present trampoline.
    * If it can be straight terminated, returns a Right, otherwise a Left with the continuation
    *
    */
  final private def resume: Either[() => Trampoline[A], A] = this match {
    case Done(a) => Right(a)
    case More(cont) => Left(cont)
    case FlatMap(a, f) => a match {
      case Done(va) => f(va).resume
      case More(cont) => Left(() => FlatMap(cont(), f))
      case FlatMap(t, cont) => (FlatMap(t, (x: Any) => FlatMap(cont(x), f)): Trampoline[A]).resume
      // last FlatMap is just rewritten following Monad laws:
      // f: A => F[B]
      // g: B => F[C]
      //  ma.flatMap(f).flatMap(g) = ma.flatMap(a => f(a).flatMap(g))
    }

  }

}

case class More[+A](cont: () => Trampoline[A]) extends Trampoline[A]
case class Done[+A](result: A) extends Trampoline[A]
//express the flatmap operation through a type
case class FlatMap[A, +B](sub: Trampoline[A], cont: A => Trampoline[B]) extends Trampoline[B]

object TestState {
  import State._
  def zipIndex[A](as: List[A]): List[(Int, A)] = as.foldLeft(
    pureState[Int, List[(Int,A)]](List.empty[(Int, A)])) ( (acc,a) => for {
    xs <- acc
    n <- getState
    _ <- setState(n + 1)
  } yield (n,a)::xs).runS(0)._1.reverse

}

object Runner extends App {
  println(TestState.zipIndex("ABCDEFGHIJKLMNOPQRDSTUVWXYZ".toList))
//  Test.zipIndex((1 to 4500).toList) //SOF

}
