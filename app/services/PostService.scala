package services

import akka.stream.scaladsl.Source
import models.forum.Post
import play.api.libs.json._

import scala.concurrent.duration._

/**
  * Created by Sandeep.K on 9/4/2016.
  */
trait PostService {

  def postSource: Source[JsValue, _] = {
    val tickSource = Source.tick(0 millis, 10000 millis, "TICK")
    val s = tickSource.map((tick) => {
      Json.toJson(Post.findAll)
    })
    s
  }

}
