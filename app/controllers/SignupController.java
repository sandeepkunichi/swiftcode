package controllers;

import actions.ValidationAction;
import forms.RegisterForm;
import forms.SignupForm;
import models.AppUser;
import models.CandidateInformation;
import models.Registration;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.SessionService;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;

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
    @ValidationAction.ValidationActivity(validationActionType = SignupForm.class)
    public Result signup(){
        Form<SignupForm> userForm = formFactory.form(SignupForm.class).bindFromRequest();
        if(configuration.getBoolean("closedSignup")){
            Result result = closedLogin(userForm);
            if(result != null){
                return result;
            }
        }
        AppUser appUser = new AppUser(userForm.data().get("email"), userForm.data().get("password"));
        appUser.candidateInformation = new CandidateInformation();
        CandidateInformation.db().save(appUser.candidateInformation);
        AppUser.db().save(appUser);
        sessionService.storeUserInSession(appUser);
        return redirect("/dashboard");
    }

    @NoAuthRequired
    @ValidationAction.ValidationActivity(validationActionType = RegisterForm.class)
    public Result register() throws IOException {
        Form<RegisterForm> registerForm = formFactory.form(RegisterForm.class).bindFromRequest();
        Registration registration = new Registration(registerForm.data().get("name"), registerForm.data().get("email"));
        Registration.db().insert(registration);

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> resume = body.getFile("resume");
        java.io.File file = resume.getFile();
        if(!Objects.equals(resume.getFilename(), "")){
            String uploadDirectory = configuration.getString("drive.resume.directory");
            Files.createDirectories(Paths.get(uploadDirectory));
            byte[] data = Files.readAllBytes(file.toPath());
            File resumeFile = new File(uploadDirectory + registerForm.data().get("email") + ".pdf");
            Boolean fileCreated = resumeFile.createNewFile();
            if(!fileCreated){
                return ok("Done! We will get back to you soon.");
            }
            OutputStream out = new FileOutputStream(resumeFile);
            out.write(data);
            out.close();
        }
        return ok("Done! We will get back to you soon.");
    }

    public Result closedLogin(Form<SignupForm> userForm){
        if(!configuration.getList("allowedSignups").stream().anyMatch(x->x.toString().toLowerCase().equals(userForm.data().get("email").toLowerCase()))){
            userForm.errors().put(
                    "message",
                    Collections.singletonList(new ValidationError("message", "This email is not recognized. Please use the email address where you received the signup link"))
            );
            return ok(views.html.shared.signup.render(userForm.errorsAsJson()));
        }
        return null;
    }

}
