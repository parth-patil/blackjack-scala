package blackjack

class BlackJackGame(d: Deck) {
  import BlackJackGame._

  val deck: Deck = d

  val playerHand = new Hand
  val dealerHand = new Hand

  // Variables to represent the game state
  protected var turn: String = TurnPlayer
  protected var winner: String = WinnerUndecided

  /**
   * Starts a game
   */
  def startGame() {
    // Deal two cards to dealer
    dealerHand.addCard(deck.dealCard())
    dealerHand.addCard(deck.dealCard())

    // Deal two cards to player
    playerHand.addCard(deck.dealCard())
    playerHand.addCard(deck.dealCard())

    // Check for winner
    if (dealerHand.value() == 21 && playerHand.value() == 21)
      setWinner(WinnerDraw)
    else if (dealerHand.value() == 21)
      setWinner(WinnerDealer)
    else if (playerHand.value() == 21)
      setWinner(WinnerPlayer)
  }

  /**
   * Ask the dealer to hit
   */
  def playerHit() {
    if (turn != TurnPlayer)
      throw new BlackJackGameException("Not Player's turn")

    playerHand.addCard(deck.dealCard())

    if (playerHand.value() > 21)
      setWinner(WinnerDealer)
  }

  /**
   * Decide to stay
   */
  def playerStand() {
    if (turn != TurnPlayer)
      throw new BlackJackGameException("Not Player's turn")

    turn = TurnDealer
  }

  /**
   * Ask the dealer to deal cards until hand value
   * is greater than or equal to DealerStandValue
   */
  def dealerPlay() {
    if (turn != TurnDealer)
      throw new BlackJackGameException("Not Dealer's turn")

    while (dealerHand.value() < DealerStandValue)
      dealerHand.addCard(deck.dealCard())

    val dealerHandValue = dealerHand.value()

    if (dealerHandValue > 21 || playerHand.value() > dealerHandValue)
      setWinner(WinnerPlayer)
    else if (dealerHandValue >= playerHand.value())
      setWinner(WinnerDealer)
  }

  protected def setWinner(w: String) {
    winner = w
    turn = TurnNobody
  }

  def gameStatus(): Map[String, String] = {
    def dealerHandString(): String = {
      if (isOver)
        dealerHand.toString()
      else {
        dealerHand.toStringWithFirstCardHidden()
      }
    }

    Map(
      "playerHand" -> playerHand.toString(),
      "dealerHand" -> dealerHandString(),
      "winner"     -> winner.toString(),
      "turn"       -> turn.toString(),
      "isGameOver" -> (if (isOver) "Yes" else "No")
    )
  }

  def isOver: Boolean = if (winner == WinnerUndecided) false else true

  def resetGame() {
    playerHand.clear()
    dealerHand.clear()
    turn = TurnPlayer
    winner = WinnerUndecided
  }
}

object BlackJackGame {
  // Constants to indicate whose turn it is to play
  val TurnPlayer = "TurnPlayer"
  val TurnDealer = "TurnDealer"
  val TurnNobody = "TurnNobody"

  // Constants to indicate who won
  val WinnerPlayer    = "WinnerPlayer"
  val WinnerDealer    = "WinnerDealer"
  val WinnerDraw      = "WinnerDraw"
  val WinnerUndecided = "WinnerUndecided"

  val DealerStandValue = 17 // Value at which dealer has to stop dealing to him/her self

  def createDefaultBlackJackGame(): BlackJackGame = {
    val deck = Deck.getNormalDeck()
    new BlackJackGame(deck)
  }

  /**
   * The factory method helps with testing
   * We can pass in custom Decks that can be used
   * to test different conditions
   *
   * @param d
   * @return
   */
  def createBlackJackGameWithDeck(d: Deck): BlackJackGame = new BlackJackGame(d)
}

class BlackJackGameException(msg: String) extends Exception(msg)