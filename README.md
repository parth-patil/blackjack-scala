# Project Blackjack

This project contains a Blackjack game implemented as a client and server application.
The server is written using Finagle and the server exposes a thrift interface.
The server supports multiple players playing at the same time.

# Playing Blackjack
First you will need to clone this project. Next you need to run the server and the
client as follows

## Running the Blackjack Finagle Server
```
cd blackjack
./sbt
> compile
> run -f config/development.scala
```

## Running Blackjack client
```
cd blackjack
./console 127.0.0.1 9999
```

