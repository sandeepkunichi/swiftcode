package controllers.admin;

import play.mvc.Controller;
import play.mvc.Result;
import services.StatsService;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Sandeep.K on 9/25/2016.
 */
public class StatsController extends Controller {

    @Inject
    StatsService statsService;

    @AdminOnly
    public Result getTestStats(Long testId) throws IOException {
        return ok(views.html.admin.test_stats.render(statsService.getTestStats(testId)));
    }

    @AdminOnly
    public Result getUserStats(Long userId) {
        return ok(views.html.admin.user_stats.render(statsService.getUserStats(userId)));
    }

}
