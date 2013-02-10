package com.twitter.blackjack

import com.twitter.logging.Logger
import com.twitter.ostrich.admin.{RuntimeEnvironment, ServiceTracker}

object Main {
  private val log = Logger.get(getClass)

  def main(args: Array[String]) {
    val runtime = RuntimeEnvironment(this, args)
    val server = runtime.loadRuntimeConfig[BlackjackService.ThriftServer]
    try {
      log.info("Starting BlackjackService")
      server.start()
    } catch {
      case e: Exception =>
        log.error(e, "Failed starting BlackjackService, exiting")
        ServiceTracker.shutdown()
        System.exit(1)
    }
  }
}
