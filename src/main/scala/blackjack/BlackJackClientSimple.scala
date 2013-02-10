package blackjack

object BlackJackClientSimple {
  def play(): Map[String, String] = {
    val game = BlackJackGame.createDefaultBlackJackGame();
    game.startGame()
    printStatus(game.gameStatus())

    if (game.isOver) {
      return game.gameStatus()
    }

    // Keep Hitting
    while (getPlayerChoice() == 'H' && !game.isOver) {
      game.playerHit()
      printStatus(game.gameStatus())
    }

    if (game.isOver)
      return game.gameStatus()

    // If game is not over player selected to Stay
    game.playerStand()

    if (game.isOver)
      return game.gameStatus()

    // Let the dealer deal
    while (!game.isOver) {
      game.dealerPlay()
      printStatus(game.gameStatus())
    }

    return game.gameStatus()
  }

  def printStatus(gameStatus: Map[String, String]) {
    println("Dealer Hand : " + gameStatus("dealerHand"))
    println("Player Hand : " + gameStatus("playerHand"))
  }

  def getPlayerChoice(): Char = {
    println("Hit or Stay ? (H/S)")
    var choice = ' '

    do {
      choice = Console.readChar().toUpper
      if (choice != 'H' && choice != 'S') {
        MyConsole.error("Unknown choice , plese select H or S")
      }
    } while (choice != 'H' && choice != 'S')

    choice
  }

  def main(args: Array[String]) {
    var playerBalance = 100

    var playAgain = true
    do {
      println("Current balance = " + playerBalance)

      val bet = getBetAmt(playerBalance)
      println("Your bet = " + bet)

      // play game and adjust balance based on result
      val gameStatus = play()
      gameStatus("winner") match {
        case BlackJackGame.WinnerDealer => MyConsole.success("Dealer Won"); playerBalance -= bet
        case BlackJackGame.WinnerPlayer => MyConsole.success("You Won!"); playerBalance += bet
        case BlackJackGame.WinnerDraw => MyConsole.success("Draw")
        case _ => throw new Exception("Unknown result encountered ")
      }

      if (playerBalance == 0) {
        MyConsole.warn("You have no money! Good Bye !")
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
        MyConsole.error("Bet can't be greater than current Balance ! Try again ")
    } while (bet > balance)

    bet
  }
}

