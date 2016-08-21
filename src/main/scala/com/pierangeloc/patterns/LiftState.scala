package com.pierangeloc.patterns

/**
  *
  * scala-playground - 28/07/16
  * Created with ♥ in Amsterdam
  */
object LiftState {
import scalaz._
  import Scalaz._

  type ListState[A] = State[List[String], A]

  1.point[ListState]

  // lifting state:
  def lift[T, S, A](s: State[T, A],
                    get: S => T, //given initial S, calculate initial T
                    set: (S, T) => S   //given initial S, new T, calculate new S
                   ): State[S, A] = {
    State { inputS =>
      val inputT: T = get(inputS)
      val (outputT, a) = s.run(inputT)
      val outputS = set(inputS, outputT)
      (outputS, a)
    }
  }

  def liftLens[T, S, A](s: State[T, A],
                        lens: Lens[S, T]
                    //lens.get: given initial S, calculate initial T
                    //lens.set: given initial S, new T, calculate new S
                   ): State[S, A] = {
    State { inputS =>
      val inputT: T = lens.get(inputS)
      val (outputT, a) = s.run(inputT)
      val outputS = lens.set(inputS, outputT)
      (outputS, a)
    }
  }
}
