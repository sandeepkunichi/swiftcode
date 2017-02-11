package services

import java.time.{ZoneId, ZonedDateTime}
import java.util.Date

import akka.stream.scaladsl.Source

import scala.concurrent.duration._
import scala.language.postfixOps

trait Ticker {

  def stringSource(start: Long, duration: Long): Source[String, _] = {
    val startTime = ZonedDateTime.ofInstant(new Date(start).toInstant, ZoneId.systemDefault())
    val tickSource = Source.tick(0 millis, 100 millis, "TICK")
    tickSource.map((tick) => String.valueOf(startTime.plusMinutes(duration).toInstant.getEpochSecond - ZonedDateTime.now().toInstant.getEpochSecond))
  }

}