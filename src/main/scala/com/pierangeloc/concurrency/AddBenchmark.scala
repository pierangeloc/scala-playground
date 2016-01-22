package com.pierangeloc.concurrency

import java.util.concurrent.{Callable, Executors, ExecutorService}
import java.util.concurrent.locks.ReentrantLock

import scala.annotation.tailrec
import scala.collection.parallel.ParallelCollectionImplicits

/**
 * Benchmark application to compare performances of approaches to simply increase a counter
 */
object AddBenchmark extends App {

  final val MAX: Integer = 10000000
  benchmark(incrementWithList, "List")
  benchmark(incrementWithWhile, "While Loop")
  benchmark(incrementWithTailRec, "Tail Recursion")
  benchmark(incrementWithStream, "Scala Stream")
  benchmark(incrementWithWhileAndLock, "While Loop with lock/unlock around incrementation")
  benchmark(incrementWithWhileAndSynchronized, "While Loop with synchronized around incrementation")

  def benchmark[T](code: => T, description: String) = {
    val now = System.currentTimeMillis()
    val result = code
    println(s"adding with $description produced result: $result and took ${System.currentTimeMillis() - now} ms")
  }

  def incrementWithStream = {
    val interval = Stream.fill(MAX)(1)
    interval.reduce(_ + _): Integer
  }

  def incrementWithList = {
    val interval = List.fill(MAX)(1)
    interval.reduce(_ + _): Integer
  }

  def incrementWithWhile = {
    var counter = 0
    var acc = 0

    while (counter < MAX) {
      acc += 1
      counter += 1
    }
    acc
  }

  def incrementWithTailRec = {
    @tailrec
    def addTailRec(acc: Integer): Integer = {
      if(acc >=  MAX) acc
      else addTailRec(acc + 1)
    }
    addTailRec(0)
  }

  def incrementWithWhileAndLock = {
    var counter = 0
    var acc = 0
    val lock = new ReentrantLock
    while (counter < MAX) {
      lock.lock()
      acc += 1
      counter += 1
      lock.unlock()
    }
    acc
  }

  def incrementWithWhileAndSynchronized = {
    var counter = 0
    var acc = 0
    val lock = new AnyRef
    while (counter < MAX) {
      lock.synchronized {
        acc += 1
        counter += 1
      }
    }
    acc
  }

//  def incrementInParallelWith4ThreadsWithoutSynchronization = {
//    var integerSingleton = (0:Integer,)
//    val vector = (1,2,3,4)
//
//    val executors = Executors.newFixedThreadPool(4)
//    val callable = () => new Callable[Tuple1[Integer]] {
//      override def call(): Tuple1[Integer] = {
//        integerSingleton = (integerSingleton._1 + 1,)
//        integerSingleton
//      }
//    }
//
//    val callables = List.fill(MAX)(callable)
////    callables
//    executors.invokeAll(callables)
//
//    ParallelCollectionImplicits
//
//  }


}
