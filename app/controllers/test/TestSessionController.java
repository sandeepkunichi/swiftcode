package controllers.test;

import actions.ValidationAction;
import controllers.admin.AdminOnly;
import data.types.DashboardAlertType;
import models.AppUser;
import models.test.Test;
import models.test.TestSession;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.AppUserService;
import services.SessionService;
import services.TestSessionService;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
public class TestSessionController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    @Inject
    AppUserService appUserService;

    @Inject
    TestSessionService testSessionService;

    public Result startTest(Long testId) throws IOException {
        AppUser loggedInUser = sessionService.getSessionUser();

        if(Test.find.byId(testId) == null)
            return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.TEST_NOT_FOUND));

        if(appUserService.hasTakenTest(loggedInUser.id, testId))
            return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.TEST_ALREADY_TAKEN));

        TestSession testSession = testSessionService.createTestSession(testId, loggedInUser);

        return ok(views.html.test.test_session.render(testSessionService.randomizeTestSession(testSession)));
    }

    @ValidationAction.ValidationActivity(validationActionType = TestSession.class)
    public Result submitTest(){
        Form<TestSession> testSessionForm = formFactory.form(TestSession.class).bindFromRequest();

        TestSession testSession = testSessionForm.get();

        TestSession.db().update(testSessionService.evaluateTestSession(testSession));

        return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.TEST_SUBMISSION_SUCCESS));
    }

    @AdminOnly
    public Result deleteSession(Long sessionId){
        testSessionService.deleteSession(sessionId);
        return ok();
    }

}
