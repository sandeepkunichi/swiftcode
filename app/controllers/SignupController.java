package controllers;

import forms.SignupForm;
import models.AppUser;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.AppUserService;
import services.SessionService;

import javax.inject.Inject;

/**
 * Created by Sandeep.K on 8/15/2016.
 */
public class SignupController extends Controller{

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    @NoAuthRequired
    public Result loadSignupPage(){
        return ok(views.html.shared.signup.render(null));
    }

    @NoAuthRequired
    public Result signup(){
        Form<SignupForm> userForm = formFactory.form(SignupForm.class).bindFromRequest();
        if(userForm.hasErrors()){
            return ok(views.html.shared.signup.render(userForm.errorsAsJson()));
        }else{
            AppUser appUser = new AppUser(userForm.data().get("email"), userForm.data().get("password"));
            AppUser.db().save(appUser);
            sessionService.storeUserInSession(appUser);
        }
        return redirect("/dashboard");
    }
}
