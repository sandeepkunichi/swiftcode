package controllers.admin;

import models.AppUser;
import models.test.Test;
import models.test.TestSession;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
        List<TestSession> testSessions = TestSession.find.all().stream().map(testSession -> {
            testSession.test = Test.find.byId(testSession.test.id);
            testSession.testTaker = AppUser.find.byId(testSession.testTaker.id);
            return testSession;
        }).sorted((t1, t2) -> Long.compare(t2.score, t1.score)).collect(Collectors.toList());
        return ok(views.html.admin.index.render(tests, sessionService.getSessionUser(), users, testSessions));
    }

    public Result createTest(){
        Form<Test> testForm = formFactory.form(Test.class).bindFromRequest();
        Test test = testForm.get();
        Test.db().insert(test);
        return redirect("/");
    }

}
