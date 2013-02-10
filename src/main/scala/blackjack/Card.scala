package blackjack

object Suite extends Enumeration {
  type Suite = Value

  val CLUBS    = Value("C")
  val DIAMONDS = Value("D")
  val HEARTS   = Value("H")
  val SPADES   = Value("S")

  def getSuiteList: List[Suite] = {
    List(CLUBS, DIAMONDS, HEARTS, SPADES)
  }

  /**
   * For printing UTF8 chars
   * @param suite
   * @return
   */
  def getUTF8char(suite: Suite): Char = suite match {
    case CLUBS => 9827.toChar
    case DIAMONDS => 9830.toChar
    case HEARTS => 10084.toChar
    case SPADES => 9824.toChar
  }
}

import Suite._
case class Card(suite: Suite, value: Int) {
  override def toString(): String = {
    System.getProperty("file.encoding") match {
      case "UTF-8" => Suite.getUTF8char(suite) + " " + getValueAsString(value)
      case _ => suite + ":" + getValueAsString(value)
    }
  }

  def getValueAsString(value: Int): String = value match {
    case 1  => "Ace"
    case 11 => "Jack"
    case 12 => "Queen"
    case 13 => "King"
    case _ => value.toString
  }
}

