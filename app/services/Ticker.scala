package services

import java.time.ZonedDateTime

import akka.stream.scaladsl.Source

import scala.concurrent.duration._

trait Ticker {

  def stringSource(start: ZonedDateTime): Source[String, _] = {
    val tickSource = Source.tick(0 millis, 100 millis, "TICK")
    val end = start.plusMinutes(30)
    val s = tickSource.map((tick) => String.valueOf(end.toInstant.getEpochSecond - ZonedDateTime.now().toInstant.getEpochSecond))
    s
  }

}