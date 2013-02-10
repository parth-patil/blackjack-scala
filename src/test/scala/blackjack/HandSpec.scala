package blackjack

import org.scalatest._

class HandSpec extends FunSpec {
  describe("A Hand") {
    it("should have value 12 with 2 aces") {
      val hand = new Hand
      hand.addCard(Card(Suite.CLUBS, 1))
      hand.addCard(Card(Suite.HEARTS, 1))

      assert(hand.value === 12)
    }

    it("should have value 13 with 3 aces") {
      val hand = new Hand
      hand.addCard(Card(Suite.CLUBS, 1))
      hand.addCard(Card(Suite.HEARTS, 1))
      hand.addCard(Card(Suite.SPADES, 1))

      assert(hand.value === 13)
    }

    it("should have value 21 with 1 ace and 1 king") {
      val hand = new Hand
      hand.addCard(Card(Suite.CLUBS, 1))
      hand.addCard(Card(Suite.HEARTS, 10))

      assert(hand.value === 21)
    }

    it("should have value 21 with 1 ace and 2 kings") {
      val hand = new Hand
      hand.addCard(Card(Suite.CLUBS, 1))
      hand.addCard(Card(Suite.HEARTS, 10))
      hand.addCard(Card(Suite.SPADES, 10))

      assert(hand.value === 21)
    }

    it("should have value 19 with 8, 7 & 4") {
      val hand = new Hand
      hand.addCard(Card(Suite.CLUBS, 8))
      hand.addCard(Card(Suite.HEARTS, 7))
      hand.addCard(Card(Suite.SPADES, 4))

      assert(hand.value === 19)
    }

  }
}
