package blackjack

object MyConsole {
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
