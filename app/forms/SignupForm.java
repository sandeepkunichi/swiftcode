package forms;

import models.AppUser;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeep.K on 8/15/2016.
 */
public class SignupForm {

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        AppUser appUserWithSameEmail = AppUser.find.where().eq("email", email).findUnique();
        if (appUserWithSameEmail != null) {
            errors.add(new ValidationError("message", "This email is already registered."));
        }
        return errors;
    }

}
