package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by Sandeep.K on 22-02-2017.
 */
public class Application extends Controller {
    @NoAuthRequired
    public Result index() {
        return redirect("https://betsol.com");
    }

    @NoAuthRequired
    public Result swiftcode() {
        return ok(views.html.site.swiftcode.render());
    }

    @NoAuthRequired
    public Result innovations() {
        return ok(views.html.site.innovations.render());
    }
}
