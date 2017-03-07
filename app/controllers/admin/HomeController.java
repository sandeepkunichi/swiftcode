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
        return ok(views.html.admin.index.render(
                testService.findAllTests(),
                sessionService.getSessionUser(),
                appUserService.findAllAppUsers(),
                testSessionService.findAllTestSessions(),
                Registration.find.all(),
                Language.find.all()
        ));
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
