package controllers.dashboard

import javax.inject.Singleton

import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.mvc._
import services.PostService

/**
  * Created by Sandeep.K on 9/4/2016.
  */

@Singleton
class PostController extends Controller with PostService {

  def streamPosts() = Action {
    Ok.chunked(postSource via EventSource.flow).as(ContentTypes.EVENT_STREAM)
  }

}
