package com.twitter.blackjack

import com.twitter.conversions.time._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import java.net.InetSocketAddress
import blackjack._

object BlackjackConsoleClient extends App {
  val service = ClientBuilder()
    .hosts(new InetSocketAddress(args(0), args(1).toInt))
    .codec(ThriftClientFramedCodec())
    .hostConnectionLimit(1)
    .tcpConnectTimeout(3.seconds)
    .build()

  val client = new BlackjackService.FinagledClient(service)
  val token: String = client.startSession(depositAmount = 100).get

  var playAgain = true
  do {
    val balance = client.getBalance(token).get
    println("Current balance = " + balance)

    val betAmount = getBetAmt(balance)
    println("Your bet = " + betAmount)

    // play game and adjust balance based on result
    val gStatus = play(betAmount)
    gStatus("winner") match {
      case BlackJackGame.WinnerDealer => MyConsole.success("Dealer Won")
      case BlackJackGame.WinnerPlayer => MyConsole.success("You Won!")
      case BlackJackGame.WinnerDraw => MyConsole.success("Draw")
      case _ => throw new Exception("Unknown result encountered ")
    }

    val balanceAfter = client.getBalance(token).get
    if (balanceAfter == 0) {
      MyConsole.warn("You have no money! Good Bye !")
      System.exit(0)
    }

    println("New Balance = " + balanceAfter)

    playAgain = getPlayAgainChoice() == "Y"

  } while (playAgain)

  println("Bye! Thanks for Playing")
  System.exit(0)

  def gameStatus(): scala.collection.Map[String, String] = client.getGameStatus(token).get()

  def play(betAmount: Int): scala.collection.Map[String, String] = {
    def isGameOver(): Boolean = {
      val status = gameStatus()
      status("isGameOver") == "Yes"
    }

    client.beginGame(token, betAmount).get
    printGameStatus

    if (isGameOver())
      return gameStatus()

    // Keep Hitting
    while (!isGameOver() && getHitOrStand() == "H") {
      client.hit(token).get
      printGameStatus
    }

    if (isGameOver())
      return gameStatus()

    // If game is not over player selected to Stand
    client.stand(token).get

    printGameStatus

    return gameStatus()
  }

  def printGameStatus() {
    val gs = gameStatus()
    println("Dealer Hand : " + gs("dealerHand"))
    println("Player Hand : " + gs("playerHand"))
  }

  def getHitOrStand(): String = {
    println("Hit or Stand ? (H/S)")
    var choice = ""

    do {
      choice = Console.readLine().trim().toUpperCase
      if (choice != "H" && choice != "S") {
        MyConsole.error("Unknown choice , please select H or S")
      }
    } while (choice != "H" && choice != "S")

    choice
  }

  def getBetAmt(balance: Int): Int = {
    println("How much you want to bet ? ")
    var bet = 0

    do {
      try {
        bet = Console.readLine().trim.toInt
      } catch {
        case e => MyConsole.error("Bet is not an Int, Try again")
      }

      if (bet == 0)
        MyConsole.error("Bet can't 0 ! Try again ")

      if (bet > balance)
        MyConsole.error("Bet can't be greater than current Balance ! Try again ")
    } while (bet > balance || bet == 0)

    bet
  }

  def getPlayAgainChoice(): String = {
    println("Play Again ? (Y/N) ")

    var choice = "Y"
    do {
      choice = Console.readLine().trim().toUpperCase
      if (choice != "Y" && choice != "N") {
        MyConsole.error("Unknown choice, please select Y or N")
      }
    } while (choice != "Y" && choice != "N")

    choice
  }
}
