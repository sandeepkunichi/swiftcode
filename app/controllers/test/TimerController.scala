package controllers.test

import java.time.{ZoneId, ZonedDateTime}
import javax.inject.Singleton

import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.mvc._
import services.Ticker
import models.test.TestSession

@Singleton
class TimerController extends Controller with Ticker {

  def streamClock(id: Long) = Action {
    val testSession = TestSession.find.byId(id)
    val start = ZonedDateTime.ofInstant(testSession.startTime.toInstant, ZoneId.systemDefault())
    Ok.chunked(stringSource(start) via EventSource.flow).as(ContentTypes.EVENT_STREAM)
  }

}
