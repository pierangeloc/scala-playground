package com.pierangeloc.typelevel

/**
  * Created by pierangelo.cecchetto on 15/05/16.
  */
object Hacking {

  /**
    * 1. HIGHER KINDED TYPES
    */
  //values -> types -> kinds
  // values are to types as types are to kinds, i.e. kinds classify types
  /**
    * (Note: :: is meant to define the "has Kind"
    *
    * Things on the right hand of a column, have kind "type". This isn' very useful.
    *
    * type Int :: *
    * type (Int) => String :: *
    *
    * Types of a kind different than "type"?
    *
    * E.g List can be used as a type, but it requires a type. So it is a type that has a different kind. The kind of List
    * is type => type
    * type List :: * => *
    * type Function1 :: (* x *) => * because it takes 2 types (FROM, TO) and builds another type (FROM => TO)
    * They are type constructor: take one or two types and make a new type.
    * Suggestion: Think of the type constructors as functions on the type level.
    */

  //function on the value level
  //id: Int => Int
  def id(x: Int) = x

  // => Equivalent on type level:
  //function on the type level: its  kind is "Type to type"
  //Id: * => *
  type Id[A] = A

  //Function of two parameters:
  // take function and parameter and applies function to parameter
  // idd is a _higher order function_ because it takes a function as a parameter
  //idd : ((Int => Int), Int) => Int
  def idd(f: Int => Int, x: Int) = f(x)

  // => Equivalent on type level:
  //Idd is a  higher-kinded type: It takes a type constructor as parameter
  //Idd: (* => *) x * => *
  type Idd[A[_], B] = A[B]





  // application: I have a Map[Option[T], List[T]], etherogeneous.
  // So this map can contain a (k,v) where
  // key is Option[String] and v is list[String],
  // but also Option[Boolean] -> List[Boolean]

  //Take 2 type parameters, of kind * => * (K and V are type constructors)
  class HOMap[K[_], V[_]](delegate: Map[K[Any], V[Any]]) {
    //infers A out of the A of the key
    def apply[A](key: K[A]): V[A] = delegate(key.asInstanceOf[K[Any]]).asInstanceOf[V[A]]
  }

  object HOMap {
    //we _must_ use forSome to enforce that K[_] and V[_] are kinded on the same A
    type Pair[K[_], V[_]] = (K[A], V[A]) forSome { type A }

    def apply[K[+_], V[+_]](tuples: Pair[K, V]*) = {
      new HOMap[K, V](Map(tuples: _*))
    }
  }


  //this allows to simplifiy these situations:
  val map: Map[Option[Any], List[Any]] = Map(
    Some("foo") -> List("foo", "bar", "baz"),
    Some(true) -> List(true, false, false),
    Some(42) -> List(1,2,3,4)
  )
  val xs = map(Some("foo")).asInstanceOf[List[String]]
  val bs = map(Some(true)).asInstanceOf[List[Boolean]]

  //to:

  val hoMap = HOMap(
    Some("foo") -> List("foo", "bar", "baz"),
    Some(true) -> List(true, false, false),
    Some(42) -> List(1,2,3,4)
  )
  val xs2: List[String] = hoMap(Some("foo"))
  val bs2: List[Boolean] = hoMap(Some(true))


  /**
    * 2. TYPE CLASSES
    *
    * Think instead of Type category: it categorize types
    * Polimorphism
    *
    */

  //Use case: define a function sum(l: List[A]): A that works as long as A is numeric, but fails as soon as A is not (i.e. works with Int, Double, but not with String or Map)

  //Traditional solution:
  trait Num[A] {
    val zero: A
    def add(x: A, y: A): A
  }

//  def sum[A](nums: List[A])(tc: Num[A]) = nums.foldLeft(tc.zero)(tc.add)
  //and can provide Num[Int] Num[Double] and pass it to the sum function
  //CONS: must _explicitly_ provide the Num[_]

  //redefine: compiler will look in the scope for an implicit instance of Num[A] and inject it
  // I provide implicit instances of Num[Int], Num[Double]
  def sum[A](nums: List[A])(implicit tc: Num[A]) = nums.foldLeft(tc.zero)(tc.add)

  //Num is categorizing the types into those that allow our implementation or those that don't allow this
  //Numeric, Ordering are type classes in Scala.

  //If have many types, with shared commmonalities, put those commonalities in a type class and provide instances for
  //each of those classes


  /**
    * 3. Type-Level Encodings
    * Can we define a data structure on the type level?
    * HList works like a tuple
    * type system keeps track of where types are @ compile time, and runtime of what values are
    */
  object HList {
    //HList has no type parameter (generic), because there is no generic type for the whole list
    //define head and tail fn at type level and at value level, and also append equivalent
    //so we have head and tail at value level and at type level, that's why we duplicate on type and on def level
    //
    sealed trait HList {
      type Head
      type Tail <: HList
      //defines the type of appending a list of type L to `this` hlist
      type Append[L <: HList] <: HList

      def head: Head
      def tail: Tail
      def ++[L <: HList](xs: L): Append[L]
    }

    final class HNil extends HList {
      type Head = Nothing
      type Tail = Nothing
      type Append[L <: HList] = L

      def head = sys.error("Head of HNil")
      def tail = sys.error("Tail of HNil")

      def ::[A](a: A) = HCons(a, this)

      //Append xs to an HNil gives xs
      //N.B. we defined also a corresponding operation on the type level
      def ++[L <: HList](xs: L) = xs
    }

    case class HCons[A, B <: HList](head: A, tail: B) extends HList {
      type Head = A
      type Tail = B

      // This is worth a few words:
      // The Append of an L list to the current list,
      // takes the head, then recursively takes the tail of our List and appends the given list to it.
      // This is why we take as tail the value of appending the given list type L to the current tail, and not just
      // the given tail (B)
      type Append[L <: HList] = HCons[Head, Tail#Append[L]]

      def ::[C](c: C) = HCons(c, this)

      def ++[L <: HList](xs: L): Append[L] = HCons(head, tail ++ xs)
    }

    type ::[A, B <: HList] = HCons[A, B]

  }

}