package controllers.admin;

import forms.SignupForm;
import models.AppUser;
import models.CandidateInformation;
import models.Language;
import models.Registration;
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
    SessionService sessionService;

    @Inject
    TestSessionService testSessionService;

    @Inject
    AppUserService appUserService;

    @Inject
    TestService testService;

    @Inject
    FormFactory formFactory;

    @AdminOnly
    public Result index() throws IOException {
        return ok(views.html.admin.index.render(sessionService.getSessionUser()));
    }

    @AdminOnly
    public Result testList() {
        return ok(views.html.admin.test_list.render(testService.findAllTests()));
    }

    @AdminOnly
    public Result createTest() {
        return ok(views.html.admin.create_test.render());
    }

    @AdminOnly
    public Result userList() {
        return ok(views.html.admin.user_list.render(appUserService.findAllAppUsers()));
    }

    @AdminOnly
    public Result resultList() {
        return ok(views.html.admin.result_list.render(testSessionService.findAllTestSessions()));
    }

    @AdminOnly
    public Result registrationList() {
        return ok(views.html.admin.registration_list.render(Registration.find.all()));
    }

    @AdminOnly
    public Result languageConsole() {
        return ok(views.html.admin.language_console.render(Language.find.all()));
    }

    @AdminOnly
    public Result createUser() {
        Form<SignupForm> userForm = formFactory.form(SignupForm.class).bindFromRequest();
        AppUser appUser = new AppUser(userForm.data().get("email"), userForm.data().get("password"));
        appUser.candidateInformation = new CandidateInformation();
        CandidateInformation.db().save(appUser.candidateInformation);
        AppUser.db().save(appUser);
        return redirect("/admin");
    }

}
