package forms;

import models.Registration;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeep.K on 23-02-2017.
 */
public class RegisterForm {

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String name;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        Registration registration = Registration.find.where().eq("email", email).findUnique();
        if (registration != null) {
            errors.add(new ValidationError("message", "This email is already registered."));
        }
        return errors;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
