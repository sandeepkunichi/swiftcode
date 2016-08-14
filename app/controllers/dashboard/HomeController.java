package controllers.dashboard;

import models.test.TestSession;
import play.mvc.*;
import services.SessionService;

import javax.inject.Inject;
import java.io.IOException;

public class HomeController extends Controller {

    @Inject
    SessionService sessionService;

    public Result index() throws IOException {
        return ok(
            views.html.dashboard.index.render(TestSession.find.where().eq(
                "test_taker_id",
                sessionService.getSessionUser().id
            ).findList())
        );
    }

}
