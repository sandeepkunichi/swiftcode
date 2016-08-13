package models;

import com.avaje.ebean.Model;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
@Entity
public class AppUser extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String  email;

    @Constraints.Required
    public String password;

    public AppUser(String email, String password){
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
