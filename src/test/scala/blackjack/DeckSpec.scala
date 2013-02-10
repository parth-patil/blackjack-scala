package blackjack

import org.scalatest._

class DeckSpec extends FunSpec {
  describe("A Normal Deck") {
    it("should have 52 cards") {
      val deck = Deck.getNormalDeck()
      assert(deck.size() === 52)
    }

    it("should have value 340") {
      val deck = Deck.getNormalDeck()
      assert(deck.value() === 340)
    }

    it("should have cards in seq when doShuffle is false") {
      val deck = Deck.getNormalDeck(doShuffle = false)

      val expected = (for (i <- 1 to 52) yield deck.dealCard().value) mkString(":")
      val actual   = (for (i <- 1 to 4; j <- 1 to 13) yield j) mkString(":")

      assert(actual == expected)
    }

    it("should have 50 cards after dealing 2 cards") {
      val deck = Deck.getNormalDeck()
      deck.dealCard()
      deck.dealCard()
      assert(deck.size() === 50)
    }

    it("should have 0 cards after dealing 52 cards") {
      val deck = Deck.getNormalDeck()
      for (i <- 0 until 52) deck.dealCard()

      assert(deck.size() === 0)
    }

    // This is to test the deck gets repopulated and shuffled
    // after it gets empty
    it("should have 51 cards after dealing 53 cards") {
      val deck = Deck.getNormalDeck()
      for (i <- 0 until 53) deck.dealCard()

      assert(deck.size() === 51)
    }
  }

  describe("A Custom Deck") {
    it("should have size 10 when initialized with 10 cards") {
      val cards = for (i <- 1 to 10) yield Card(Suite.CLUBS, i)

      val deck = Deck.getCustomDeck(cards)
      assert(deck.size() === 10)
    }
  }
}
