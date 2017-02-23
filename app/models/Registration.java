package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Sandeep.K on 23-02-2017.
 */
@Entity
public class Registration extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;
    public String email;

    public static Finder<Long, Registration> find = new Finder<>(Registration.class);

    public Registration(String name, String email){
        this.name = name;
        this.email = email;
    }
}
