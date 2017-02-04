package controllers.test;

import actions.ValidationAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import controllers.admin.AdminOnly;
import models.test.CodeSessionConfiguration;
import models.test.Test;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.TestService;

import javax.inject.Inject;

/**
 * Created by Sandeep.K on 03-02-2017.
 */
public class TestController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    TestService testService;

    @AdminOnly
    @ValidationAction.ValidationActivity(validationActionType = Test.class)
    public Result createTest(){
        Form<Test> testForm = formFactory.form(Test.class).bindFromRequest();
        Test test = testForm.get();
        CodeSessionConfiguration.db().insert(test.codeSessionConfiguration);
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
        testForm.get().testQuestions.forEach(testQuestion -> testQuestion.testAnswers.forEach(testAnswer -> {
            if(testAnswer.isCorrect == null){
                testAnswer.isCorrect = false;
            }
        }));
        testForm.get().update();
        testForm.get().codeSessionConfiguration.update();
        return redirect("/");
    }

    @AdminOnly
    public Result activateTest(Long testId){
        Test test = Test.find.byId(testId);
        if (test != null) {
            test.testStatus = Test.TestStatus.ACTIVE;
            test.update();
        }
        return ok();
    }

    @AdminOnly
    public Result deactivateTest(Long testId){
        Test test = Test.find.byId(testId);
        if (test != null) {
            test.testStatus = Test.TestStatus.DRAFT;
            test.update();
        }
        return ok();
    }

    @AdminOnly
    public Result deleteTest(Long testId){
        Test test = Test.find.byId(testId);
        if (test != null) {
            test.delete();
            test.codeSessionConfiguration.delete();
        }
        return ok();
    }
}
