package controllers.admin;

import models.AppUser;
import models.test.Test;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
public class HomeController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    public Result index() throws IOException {
        List<Test> tests = Test.find.all();
        List<AppUser> users = AppUser.find.all();
        return ok(views.html.admin.index.render(tests, sessionService.getSessionUser(), users));
    }

    public Result createTest(){
        Form<Test> testForm = formFactory.form(Test.class).bindFromRequest();
        Test test = testForm.get();
        Test.db().insert(test);
        return redirect("/");
    }

}
