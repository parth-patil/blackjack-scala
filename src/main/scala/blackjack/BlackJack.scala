package blackjack

class BlackJack {
  /**
   * Plays one hand of BlackJack and returns
   * true if player won and false if
   *
   * @return
   */
  def play(): Int = {
    // Create Deck
    val deck = new Deck

    // Create Hands
    val dealerHand = new Hand
    val playerHand = new Hand

    // Deal two cards to dealer
    dealerHand.addCard(deck.dealCard())
    dealerHand.addCard(deck.dealCard())
    println("Dealer Hand -> " + "[" + dealerHand.card(0) + ", * ]")

    // Deal two cards to player
    playerHand.addCard(deck.dealCard())
    playerHand.addCard(deck.dealCard())
    println("Player Hand -> " + playerHand)

    // Check if dealer won
    if (dealerHand.value() == 21)
      return 0

    // Check if player won
    if (playerHand.value() == 21)
      return 1

    // If nobody won continue the game
    var gameOver = false
    var result = 3

    // Keep Hitting
    var choice:Char = getPlayerChoice()
    while (choice == 'H') {
      playerHand.addCard(deck.dealCard())
      println("Player Hand -> " + playerHand)

      if (playerHand.value() > 21) {
        println("Player hand Busted !")
        gameOver = true
        result = 0
        choice = ' '
      } else {
        choice = getPlayerChoice()
      }
    }

    if (gameOver)
      return result

    // Keep Dealing for Dealer
    while (dealerHand.value() < 16) {
      dealerHand.addCard(deck.dealCard())
      println("Dealer Hand -> " + dealerHand)

      if (dealerHand.value() > 21) {
        println("Dealer hand Busted !")
        gameOver = true
        result = 1
      }
    }

    if (gameOver)
      return result

    if (dealerHand.value() == playerHand.value()) {
      println("Dealer Hand -> " + dealerHand)
      return 2
    } else if (dealerHand.value() > playerHand.value()) {
      println("Dealer Hand -> " + dealerHand)
      return 0
    } else {
      println("Dealer Hand -> " + dealerHand)
      return 1
    }
  }

  def getPlayerChoice(): Char = {
    println("Hit or Stay ? (H/S)")
    var choice = ' '

    do {
      choice = Console.readChar().toUpper
      if (choice != 'H' && choice != 'S') {
        MyConsole2.error("Unknown choice , plese select H or S")
      }
    } while (choice != 'H' && choice != 'S')

    choice
  }
}

object BlackJack {
  def main(args: Array[String]) {

    val blackJack = new BlackJack
    var playerBalance = 100

    var playAgain = true
    do {
      println("Current balance = " + playerBalance)

      val bet = getBetAmt(playerBalance)
      println("Your bet = " + bet)

      // play game and adjust balance based on result
      blackJack.play() match {
        case 0 => MyConsole2.success("Dealer Won"); playerBalance -= bet
        case 1 => MyConsole2.success("You Won!"); playerBalance += bet
        case 2 => MyConsole2.success("Draw")
        case _ => throw new Exception("Unknown result encountered ")
      }

      if (playerBalance == 0) {
        MyConsole2.warn("You have no money! Good Bye !")
        System.exit(0)
      }

      println("New Balance = " + playerBalance)

      println("Play Again ? (Y/N) ")
      Console.readChar().toUpper match {
        case 'N' => playAgain = false
        case _ =>
      }
    } while (playAgain)
  }

  def getBetAmt(balance: Int): Int = {
    var bet = 0
    do {
      println("How much you want to bet ? ")
      bet = Console.readInt()
      if (bet > balance)
        MyConsole2.error("Bet can't be greater than current Balance ! Try again ")
    } while (bet > balance)

    bet
  }
}

object MyConsole2 {
  def success(msg: String) {
    println(Console.GREEN + msg + Console.RESET)
  }

  def warn(msg: String) {
    println(Console.YELLOW + msg + Console.RESET)
  }

  def error(msg: String) {
    println(Console.RED + msg + Console.RESET)
  }
}
