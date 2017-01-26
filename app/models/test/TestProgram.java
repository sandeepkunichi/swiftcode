package models.test;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Sandeep.K on 25-01-2017.
 */
@Entity
public class TestProgram extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String programQuestion;

    public TestProgram(String programQuestion){
        this.programQuestion = programQuestion;
    }

    public static Finder<Long, TestProgram> find = new Finder<>(TestProgram.class);

}
