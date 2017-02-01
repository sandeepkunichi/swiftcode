package actions;

import forms.LoginForm;
import forms.SignupForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by Sandeep.K on 30-01-2017.
 */
public class ValidationAction {

    @With(ValidationActionImpl.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidationActivity {
        Class<?> validationActionType();
    }

    public static class ValidationActionImpl extends Action<ValidationActivity> {

        @Inject
        FormFactory formFactory;

        public CompletionStage<Result> call(Http.Context context) {
            Form form = formFactory.form(configuration.validationActionType()).bindFromRequest();
            Result result;
            if(configuration.validationActionType().equals(LoginForm.class)){
                result = ok(views.html.shared.login.render(form.errorsAsJson()));
            }else if(configuration.validationActionType().equals(SignupForm.class)){
                result = ok(views.html.shared.signup.render(form.errorsAsJson()));
            }else{
                result = ok(form.errorsAsJson());
            }
            return form.hasErrors() ? CompletableFuture.completedFuture(result) : delegate.call(context);
        }

    }
}