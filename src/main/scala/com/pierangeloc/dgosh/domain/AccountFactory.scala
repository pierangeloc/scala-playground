package com.pierangeloc.dgosh.domain

import java.time.LocalDate

  object common {
    type Amount = BigDecimal

    def today = LocalDate.now()
  }

  import common._
  case class Balance(amount: Amount = 0)

  sealed trait  Account {
    def no: String
    def name: String
    def dateOfOpen: Option[LocalDate]
    def dateOfClose: Option[LocalDate]
    def balance: Balance
  }

  // private traits
  final case class CheckingAccount private[domain] (no: String, name: String, dateOfOpen: Option[LocalDate], dateOfClose: Option[LocalDate] = None, balance: Balance = Balance()) extends Account
  final case class SavingsAccount private[domain] (no: String, name: String, interestRate: Amount, dateOfOpen: Option[LocalDate], dateOfClose: Option[LocalDate] = None, balance: Balance = Balance()) extends Account

  object Account {
    def savingsAccount(no: String, name: String, dateOfOpen: Option[LocalDate],
                        dateOfClose: Option[LocalDate] = None, balance: Balance) = SavingsAccount(no, name, 3.0, dateOfOpen, dateOfClose, balance)

    def checkingAccount(no: String, name: String, dateOfOpen: Option[LocalDate],
                          dateOfClose: Option[LocalDate] = None, balance: Balance = Balance()) = CheckingAccount(no, name, dateOfClose, dateOfClose, balance)
  }



