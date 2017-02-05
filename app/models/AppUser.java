package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import data.Channel;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

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

    public Role role;

    public AppUser(String email, String password){
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.role = Role.USER;
        this.resumeSubmitted = false;
    }

    public Boolean resumeSubmitted;

    @Transient
    public String externalId;

    @Transient
    @JsonProperty("channels")
    public List<Channel> channels;

    public static Model.Finder<Long, AppUser> find = new Model.Finder<>(AppUser.class);

    public Boolean isAdmin(){
        return this.role.equals(Role.ADMIN);
    }

    public enum Role {
        @EnumValue("ADMIN")
        ADMIN,
        @EnumValue("USER")
        USER
    }
}
