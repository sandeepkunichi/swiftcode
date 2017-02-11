package controllers.test

import javax.inject.Singleton

import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.mvc._
import services.Ticker

@Singleton
class TimerController extends Controller with Ticker {

  def streamClock(start: Long, duration: Long) = Action {
    Ok.chunked(stringSource(start, duration) via EventSource.flow).as(ContentTypes.EVENT_STREAM)
  }

}
