package controllers;

import forms.LoginForm;
import models.AppUser;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.AppUserService;
import services.SessionService;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
public class LoginController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    @Inject
    AppUserService appUserService;

    @NoAuthRequired
    public Result login() {
        return ok(views.html.shared.login.render(null));
    }

    @NoAuthRequired
    public Result authenticate() throws IOException {
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class).bindFromRequest();
        if(loginForm.hasErrors()){
            return ok(views.html.shared.login.render(loginForm.errorsAsJson()));
        }
        sessionService.storeUserInSession(appUserService.getAppUserByEmail(loginForm.data().get("email")));
        if(sessionService.getSessionUser().role == AppUser.Role.ADMIN)
            return redirect("/admin");
        else
            return redirect("/dashboard");
    }

    @NoAuthRequired
    public Result logout(){
        sessionService.destroySession();
        return redirect("/login");
    }

}