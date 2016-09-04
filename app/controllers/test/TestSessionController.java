package controllers.test;

import models.AppUser;
import models.test.Test;
import models.test.TestAnswer;
import models.test.TestSession;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.AppUserService;
import services.SessionService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Date;

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

    public Result startTest(Long testId) throws IOException {
        if(Test.find.byId(testId) == null)
            return redirect("/dashboard");
        AppUser loggedInUser = sessionService.getSessionUser();
        if(appUserService.hasTakenTest(loggedInUser.id, testId))
            return redirect("/dashboard");
        TestSession testSession = new TestSession();
        testSession.startTime = new Date();
        testSession.test = Test.find.byId(testId);
        testSession.testTaker = loggedInUser;
        TestSession.db().insert(testSession);
        return ok(views.html.test.test_session.render(testSession));
    }

    public Result submitTest(){
        Form<TestSession> testSessionForm = formFactory.form(TestSession.class).bindFromRequest();
        TestSession testSession = testSessionForm.get();
        testSession.score = 0L;
        testSession.endTime = new Date();
        testSession.test.testQuestions.forEach(
                testQuestion -> testQuestion.testAnswers.forEach(
                        testAnswer -> {
                            if(testAnswer.selected != null && TestAnswer.find.byId(testAnswer.id).isCorrect && testAnswer.selected){
                                testSession.score++;
                            }
                        }
                )
        );
        TestSession.db().update(testSession);
        return redirect("/dashboard");
    }

}
