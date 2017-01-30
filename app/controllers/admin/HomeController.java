package controllers.admin;

import actions.ValidationAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

    @AdminOnly
    public Result index() throws IOException {
        return ok(views.html.admin.index.render(
                testService.findAllTests(),
                sessionService.getSessionUser(),
                appUserService.findAllAppUsers(),
                testSessionService.findAllTestSessions()
        ));
    }

    @AdminOnly
    @ValidationAction.ValidationActivity(validationActionType = Test.class)
    public Result createTest(){
        Form<Test> testForm = formFactory.form(Test.class).bindFromRequest();
        Test test = testForm.get();
        test.testStatus = Test.TestStatus.DRAFT;
        Test.db().insert(test);
        return redirect("/");
    }

    @AdminOnly
    public Result editTestView(Long testId) throws JsonProcessingException {
        Test test = Test.find.byId(testId);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(test);
        return ok(views.html.admin.edit_test.render(json));
    }

    @AdminOnly
    public Result cloneTest(Long testId) {
        testService.cloneTest(Test.find.byId(testId));
        return redirect("/admin");
    }

    @AdminOnly
    @ValidationAction.ValidationActivity(validationActionType = Test.class)
    public Result editTest(){
        Form<Test> testForm = formFactory.form(Test.class).bindFromRequest();
        testForm.get().update();
        return redirect("/");
    }

    @AdminOnly
    public Result activateTest(Long testId){
        Test test = Test.find.byId(testId);
        test.testStatus = Test.TestStatus.ACTIVE;
        test.update();
        return ok();
    }

    @AdminOnly
    public Result deactivateTest(Long testId){
        Test test = Test.find.byId(testId);
        test.testStatus = Test.TestStatus.DRAFT;
        test.update();
        return ok();
    }

    @AdminOnly
    public Result deleteSession(Long sessionId){
        testSessionService.deleteSession(sessionId);
        return ok();
    }

    @AdminOnly
    public Result deleteTest(Long testId){
        Test test = Test.find.byId(testId);
        test.delete();
        return ok();
    }

}
