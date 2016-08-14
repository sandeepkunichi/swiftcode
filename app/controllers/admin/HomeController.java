package controllers.admin;

import models.test.Test;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
public class HomeController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result index(){
        List<Test> tests = Test.find.all();
        return ok(views.html.admin.index.render(tests));
    }
    public Result loadCreateTest(){
        return ok(views.html.admin.create_test.render(formFactory.form(Test.class)));
    }
    public Result createTest(){
        Form<Test> testForm = formFactory.form(Test.class).bindFromRequest();
        Test test = testForm.get();
        Test.db().insert(test);
        return redirect("/");
    }
}
