package com.pierangeloc.concurrency

import scala.annotation.tailrec

/**
 * Created by pierangeloc on 6-3-15.
 */
object AddBenchmark extends App {

  final val MAX: Integer = 10000000
  benchmark(incrementWithList, "List")
  benchmark(incrementWithWhile, "While Loop")
  benchmark(incrementWithTailRec, "Tail Recursion")
  benchmark(incrementWithStream, "Scala Stream")

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
    val now = System.currentTimeMillis()

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



}
