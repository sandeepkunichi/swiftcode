package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by Sandeep.K on 22-02-2017.
 */
public class Application extends Controller {
    @NoAuthRequired
    public Result index() {
        return ok(views.html.site.index.render());
    }

    @NoAuthRequired
    public Result swiftcode() {
        return ok(views.html.site.index.render());
    }
}
