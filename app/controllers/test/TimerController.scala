package controllers.test

import javax.inject.Singleton

import models.test.TestSession
import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.mvc._
import services.Ticker

@Singleton
class TimerController extends Controller with Ticker {

  def streamClock(id: Long) = Action {
    Ok.chunked(stringSource(TestSession.find.byId(id)) via EventSource.flow).as(ContentTypes.EVENT_STREAM)
  }

}
