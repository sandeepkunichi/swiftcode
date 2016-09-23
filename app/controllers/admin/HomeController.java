package controllers.admin;

import models.test.Test;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.AppUserService;
import services.SessionService;
import services.TestService;
import services.TestSessionService;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
public class HomeController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    @Inject
    TestSessionService testSessionService;

    @Inject
    AppUserService appUserService;

    @Inject
    TestService testService;

    public Result index() throws IOException {
        return ok(views.html.admin.index.render(
                testService.findAllTests(),
                sessionService.getSessionUser(),
                appUserService.findAllAppUsers(),
                testSessionService.findAllTestSessions()
        ));
    }

    public Result createTest(){
        Form<Test> testForm = formFactory.form(Test.class).bindFromRequest();
        Test test = testForm.get();
        test.testStatus = Test.TestStatus.DRAFT;
        Test.db().insert(test);
        return redirect("/");
    }

    public Result activateTest(Long testId){
        Test test = Test.find.byId(testId);
        test.testStatus = Test.TestStatus.ACTIVE;
        test.update();
        return ok();
    }

    public Result deactivateTest(Long testId){
        Test test = Test.find.byId(testId);
        test.testStatus = Test.TestStatus.DRAFT;
        test.update();
        return ok();
    }

}
