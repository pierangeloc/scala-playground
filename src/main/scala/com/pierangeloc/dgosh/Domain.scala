package com.pierangeloc.dgosh

import java.time.LocalDate

import scala.util.{Random, Success, Try}

/**
  * Created by pierangeloc on 28-12-15.
  */
object Domain extends App {

  case class DateRange(from: LocalDate, to: LocalDate)
  case class Balance(amount: BigDecimal)

  trait Account {
    def number: String
    def name: String
  }
  case class CheckingAccount(override val name: String, override val number: String, balance: Balance) extends Account

  trait InterestsBearingAccount extends Account {
    def rate: BigDecimal = ???
  }

  case class SavingsAccount(name: String, number: String, balance: Balance, override val rate: BigDecimal) extends InterestsBearingAccount {
  }

  trait AccountService {
    def calculateInterest[A <: InterestsBearingAccount](account: A, period: DateRange): Try[BigDecimal] = Success(BigDecimal(Random.nextDouble()))
  }
  object AccountServiceImpl extends AccountService

  val s1 = SavingsAccount("S1", "0900", Balance(10000), 0.5)
  val s2 = SavingsAccount("S2", "0800", Balance(20000), 0.75)
  val s3 = SavingsAccount("S3", "0700", Balance(30000), 0.27)
  val dateRange = DateRange(LocalDate.parse("2015-01-01"), LocalDate.parse("2015-12-31"))
  println( List(s1, s2, s3).map(AccountServiceImpl.calculateInterest(_, dateRange)))
  println( List(s1, s2, s3).map(AccountServiceImpl.calculateInterest(_, dateRange)).foldLeft(BigDecimal(0))((a, e) => e.map(_ + a).getOrElse(a)))
  s1.copy()

//  val x = domain.CheckingAccount("", "", Some(LocalDate.now()), None)

  val y = domain.Account.checkingAccount("", "", Some(LocalDate.now()), None)
  println(y.balance)
}
