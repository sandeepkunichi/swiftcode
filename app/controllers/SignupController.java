package controllers;

import forms.SignupForm;
import models.AppUser;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;

import javax.inject.Inject;
import java.util.Collections;

/**
 * Created by Sandeep.K on 8/15/2016.
 */
public class SignupController extends Controller{

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    @Inject
    Configuration configuration;

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
            if(!configuration.getList("allowedSignups").stream().anyMatch(x->x.equals(userForm.data().get("email")))){
                userForm.errors().put(
                        "message",
                        Collections.singletonList(new ValidationError("message", "This email is not recognized. Please use the email address where you received the signup link"))
                );
                return ok(views.html.shared.signup.render(userForm.errorsAsJson()));
            }
            AppUser appUser = new AppUser(userForm.data().get("email"), userForm.data().get("password"));
            AppUser.db().save(appUser);
            sessionService.storeUserInSession(appUser);
        }
        return redirect("/dashboard");
    }

}
