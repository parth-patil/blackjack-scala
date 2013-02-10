package blackjack

import org.scalatest._

class BlackJackGameSpec extends FunSpec {
  describe("In a BlackJackGame") {
    it("Dealer WINS when Dealer gets BlackJack in first 2 cards") {
      val cards = Seq(
        Card(Suite.SPADES, 1), // Dealer card
        Card(Suite.DIAMONDS, 12), // Dealer card
        Card(Suite.CLUBS, 2), // Player card
        Card(Suite.HEARTS, 8) // Player card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      val status = blackJackGame.gameStatus()

      assert(status("isGameOver") === "Yes")
      assert(status("winner") === BlackJackGame.WinnerDealer)
    }

    it("Player LOSES when hand value > 21 after 1st Hit") {
      val cards = Seq(
        Card(Suite.CLUBS, 2), // Dealer card
        Card(Suite.HEARTS, 8), // Dealer card
        Card(Suite.SPADES, 4), // Player card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.CLUBS, 8) // Player card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      val status1 = blackJackGame.gameStatus()

      assert(status1("isGameOver") === "No")
      assert(status1("winner") === BlackJackGame.WinnerUndecided)

      blackJackGame.playerHit()
      val status2 = blackJackGame.gameStatus()

      assert(status2("isGameOver") === "Yes")
      assert(status2("winner") === BlackJackGame.WinnerDealer)
    }

    it("Player WINS after Player Stands and Dealer Busts") {
       val cards = Seq(
        Card(Suite.CLUBS, 7), // Dealer card
        Card(Suite.HEARTS, 8), // Dealer card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.SPADES, 7), // Player card
        Card(Suite.HEARTS, 10) // Dealer card (busts!)
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      blackJackGame.playerStand() // player stays at 17
      blackJackGame.dealerPlay() // dealer busts with value 25

      val status = blackJackGame.gameStatus()
      assert(status("isGameOver") === "Yes")
      assert(status("winner") === BlackJackGame.WinnerPlayer)
    }

    it("Player LOSES after Player Stands and Dealer Hits 21") {
      val cards = Seq(
        Card(Suite.CLUBS, 7), // Dealer card
        Card(Suite.HEARTS, 8), // Dealer card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.SPADES, 7), // Player card
        Card(Suite.HEARTS, 6) // Dealer card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      blackJackGame.playerStand() // player stays at 17
      blackJackGame.dealerPlay() // dealer hits BlackJack

      val status = blackJackGame.gameStatus()
      assert(status("isGameOver") === "Yes")
      assert(status("winner") === BlackJackGame.WinnerDealer)
    }

    it("Player LOSES after Player Stands and Dealer's score more than Player") {
      val cards = Seq(
        Card(Suite.CLUBS, 9), // Dealer card
        Card(Suite.HEARTS, 10), // Dealer card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.SPADES, 7) // Player card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      blackJackGame.playerStand() // player stays at 17
      blackJackGame.dealerPlay()

      val status = blackJackGame.gameStatus()
      assert(status("isGameOver") === "Yes")
      assert(status("winner") === BlackJackGame.WinnerDealer)
    }

    it("Player LOSES after Player Hits and Busts") {
      val cards = Seq(
        Card(Suite.CLUBS, 7), // Dealer card
        Card(Suite.HEARTS, 8), // Dealer card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.SPADES, 7), // Player card
        Card(Suite.HEARTS, 6) // Player card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      blackJackGame.playerHit() // player hits and busts at 23

      val status = blackJackGame.gameStatus()
      assert(blackJackGame.isOver === true)
      assert(status("winner") === BlackJackGame.WinnerDealer)
    }

    it("Dealer WINS if both Player and Dealer get same score") {
      val cards = Seq(
        Card(Suite.CLUBS, 11), // Dealer card
        Card(Suite.HEARTS, 8), // Dealer card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.SPADES, 8) // Player card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      blackJackGame.playerStand() // Player stays at 18
      blackJackGame.dealerPlay()

      val status = blackJackGame.gameStatus()
      assert(status("isGameOver") === "Yes")
      assert(status("winner") === BlackJackGame.WinnerDealer)
    }

    it("Game is DRAW if both Player and Dealer get BlackJack") {
      val cards = Seq(
        Card(Suite.CLUBS, 13), // Dealer card
        Card(Suite.HEARTS, 1), // Dealer card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.SPADES, 1) // Player card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()

      val status = blackJackGame.gameStatus()
      assert(status("isGameOver") === "Yes")
      assert(status("winner") === BlackJackGame.WinnerDraw)
    }

    it("Dealer WINS if Dealer crosses Stand value(17) & has hand value greater than player") {
      val cards = Seq(
        Card(Suite.CLUBS, 7), // Dealer card
        Card(Suite.HEARTS, 8), // Dealer card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.SPADES, 7), // Player card
        Card(Suite.HEARTS, 3) // Dealer card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      blackJackGame.playerStand() // player stays at 17
      blackJackGame.dealerPlay() // dealer hits 18

      val status = blackJackGame.gameStatus()
      assert(status("isGameOver") === "Yes")
      assert(status("winner") === BlackJackGame.WinnerDealer)
    }

    it("Player WINS if Dealer crosses Stand value(17) but has hand value less than player") {
      val cards = Seq(
        Card(Suite.CLUBS, 7), // Dealer card
        Card(Suite.HEARTS, 8), // Dealer card
        Card(Suite.DIAMONDS, 10), // Player card
        Card(Suite.SPADES, 9), // Player card
        Card(Suite.HEARTS, 2) // Dealer card
      )

      val deck = Deck.getCustomDeck(cards, doShuffle = false)
      val blackJackGame = BlackJackGame.createBlackJackGameWithDeck(deck)

      blackJackGame.startGame()
      blackJackGame.playerStand() // player stays at 19
      blackJackGame.dealerPlay() // dealer hits 17 and has to stand

      val status = blackJackGame.gameStatus()
      assert(status("isGameOver") === "Yes")
      assert(status("winner") === BlackJackGame.WinnerPlayer)
    }
  }
}
