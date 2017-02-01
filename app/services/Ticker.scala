package services

import java.time.{ZoneId, ZonedDateTime}

import akka.stream.scaladsl.Source
import models.test.{Test, TestSession}

import scala.concurrent.duration._
import scala.language.postfixOps

trait Ticker {

  def stringSource(testSession: TestSession): Source[String, _] = {
    val start = ZonedDateTime.ofInstant(testSession.startTime.toInstant, ZoneId.systemDefault())
    val tickSource = Source.tick(0 millis, 100 millis, "TICK")
    val test = Test.find.byId(testSession.test.id)
    tickSource.map((tick) => String.valueOf(start.plusMinutes(test.testDuration).toInstant.getEpochSecond - ZonedDateTime.now().toInstant.getEpochSecond))
  }

}