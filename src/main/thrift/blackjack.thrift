namespace java com.twitter.blackjack
namespace rb Blackjack

/**
 * It's considered good form to declare an exception type for your service.
 * Thrift will serialize and transmit them transparently.
 */
exception BlackjackException {
  1: string description
}

service BlackjackService {
  string startSession(1: i32 depositAmount)

  void beginGame(1: string token, 2: i32 betAmount)

  i32 getCurrentBet(1: string token)

  map<string, string> getGameStatus(1: string token)

  void hit(1: string token)

  void stand(1: string token)

  i32 getBalance(1: string token)

  i32 endSession(1: string token)

  i32 numPlayersOnline()
}
