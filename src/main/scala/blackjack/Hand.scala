package blackjack

import scala.math._
import scala.collection.mutable

class Hand {
  val cards = mutable.Buffer[Card]()
  var hasAce = false

  def addCard(card: Card) {
    if (card.value == 1)
      hasAce = true

    cards += card
  }

  /**
   * Compute value of hand by considering value of J, Q, K to be 10
   * and picking value of Ace to get max total without busting
   * @return
   */
  def value(): Int = {
    var value = cards.foldLeft(0) { (total, card) => total + min(10, card.value) }
    if (hasAce && (value + 10) <= 21)
      value += 10

    value
  }

  def card(index: Int): Card = cards(index)

  /**
   * Discards existing cards and resets hasAce flag to false
   */
  def clear() {
    cards.clear()
    hasAce = false
  }

  override def toString(): String = "[" + cards.mkString(", ") + "]" + " Value = " + value()

  def toStringWithFirstCardHidden(): String = {
    "[ * , " + cards.slice(1, cards.size).mkString(", ") + "]" + " Value = *"
  }
}