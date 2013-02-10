package com.twitter.blackjack

import com.twitter.conversions.time._
import com.twitter.logging.Logger
import com.twitter.util._
import scala.collection.mutable
import config._
import blackjack._

class BlackjackServiceImpl(config: BlackjackServiceConfig) extends BlackjackService.ThriftServer {
  val serverName = "Blackjack"
  val thriftPort = config.thriftPort
  val maxConcurrentGames = config.maxConcurrentGames
  override val tracerFactory = config.tracerFactory

  // Keeps track of concurrent games
  val games = new mutable.HashMap[String, BlackJackGame] with mutable.SynchronizedMap[String, BlackJackGame]

  // Keeps track of the player balances for the concurrent games
  val playerBalances = new mutable.HashMap[String, Int] with mutable.SynchronizedMap[String, Int]

  // The current bet that the player has made
  val playerBets = new mutable.HashMap[String, Int] with mutable.SynchronizedMap[String, Int]

  def shutdown() = {
    super.shutdown(0.seconds)
  }

  /**
   * Starts a new session where the player deposits money and
   * can play several games of Blackjack till the balance hits 0
   *
   * @param depositAmount
   * @return
   */
  def startSession(depositAmount: Int): Future[String] = {
    def getToken(): String = {
      var token: String = ""
      do {
        token = java.util.UUID.randomUUID().toString
      } while (games.contains(token))

      token
    }

    if (games.size < maxConcurrentGames) {
      val token = getToken()
      games(token) = BlackJackGame.createDefaultBlackJackGame()
      playerBalances(token) = depositAmount
      return Future(token)
    }
    else
      Future.exception(BlackjackException("Can't except any more players at this time"))
  }

  /**
   * Start a new game of Blackjack
   *
   * @param token
   * @param betAmount
   * @return
   */
  def beginGame(token: String, betAmount: Int): Future[Unit] = {
    playerBets(token) = betAmount

    // Reset the current game corresponding to the player's token
    val game = games(token)
    game.resetGame()
    game.startGame()

    updateBalanceIfGameOver(token)

    Future.Unit
  }

  /**
   * Gets the current game status
   *
   * @param token
   * @return
   */
  def getGameStatus(token: String): Future[Map[String, String]] = {
    Future(games(token).gameStatus())
  }

  /**
   * Sends a message to indicate user's choice of standing
   * @param token
   * @return
   */
  def hit(token: String): Future[Unit] = {
    games(token).playerHit()

    updateBalanceIfGameOver(token)

    Future.Unit
  }

  /**
   * Sends a message to indicate user's choice of standing
   *
   * @param token
   * @return
   */
  def stand(token: String): Future[Unit] = {
    val game = games(token)
    game.playerStand()

    // As soon as the player selects play
    game.dealerPlay()

    updateBalanceIfGameOver(token)

    Future.Unit
  }

  /**
   * Ends a player's session and returns balance amount
   *
   * @param token
   * @return
   */
  def endSession(token: String): Future[Int] = {
    val balance:Int = playerBalances(token)
    games -= token
    playerBalances -= token
    playerBets -= token

    Future(balance)
  }

  /**
   * Returns the number of players playing Blackjack currently
   * @return
   */
  def numPlayersOnline(): Future[Int] = Future(games.size)

  def getBalance(token: String): Future[Int] = Future(playerBalances(token))

  def getCurrentBet(token: String): Future[Int] = Future(playerBets(token))

  protected def updateBalanceIfGameOver(token: String) {
    val game = games(token)
    if (game.isOver) {
      val bet = playerBets(token)
      val status = game.gameStatus()
      status("winner") match {
        case BlackJackGame.WinnerDealer => playerBalances(token) -= bet
        case BlackJackGame.WinnerPlayer => playerBalances(token) += bet
        case _ =>
      }
    }
  }
}
