package blackjack

import scala.collection.mutable
import scala.util.Random
import scala.math._


class Deck (inputCards: Seq[Card] = Seq[Card]()) {
  private val cards = mutable.Buffer[Card]()

  inputCards foreach { cards += _ }

  def populateCards() {
    val suites = Suite.getSuiteList

    suites.foreach { suite =>
      for (i <- 1 to 13) {
        cards += Card(suite, i)
      }
    }
  }

  /**
   * Shuffle cards using Fisher-Yates shuffle
   */
  def shuffle() {
    val maxIndex = size() - 1
    for (i <- maxIndex until 1 by -1) {
      val range = i - 1
      val randIndex = abs(Random.nextInt) % range
      val temp = cards(i)
      cards(i) = cards(randIndex)
      cards(randIndex) = temp
    }
  }

  def dealCard(): Card = {
    if (size() == 0) {
      populateCards()
      shuffle()
    }

    cards.remove(0)
  }

  def size() = cards.size

  override def toString(): String = cards.mkString(", ")

  /**
   * Sum of values of all cards
   * If card value is greater than 10 (Jack, Queen, King)
   * its value is taken as 10
   *
   * @return
   */
  def value(): Int = cards.foldLeft(0) { (total, card) => total + min(10, card.value) }
}

object Deck {
  def getNormalDeck(doShuffle:Boolean = true): Deck = {
    val deck: Deck = new Deck();

    deck.populateCards();

    if (doShuffle)
      deck.shuffle();

    deck
  }

  def getCustomDeck(cards: Seq[Card], doShuffle: Boolean = true): Deck = {
    val deck: Deck = new Deck(cards);

    if (doShuffle)
      deck.shuffle();

    deck
  }
}
