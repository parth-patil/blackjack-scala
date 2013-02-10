package blackjack

import org.scalatest._

class BlackJackSpec extends FunSpec {
  describe("A BlackJack Game") {
    it("should print 10 of Spades correctly") {
      val card = new Card(Suite.SPADES, 10)
      assert(card.toString === "S:10")
    }

    it("should print Queen of Hearts correctly") {
      val card = new Card(Suite.HEARTS, 12)
      assert(card.toString === "H:Queen")
    }
  }
}