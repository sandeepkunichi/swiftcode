package models.test;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
@Entity
public class TestQuestion extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String question;

    @Constraints.Required
    @OneToMany(cascade = CascadeType.ALL)
    public List<TestAnswer> testAnswers;

    public static Finder<Long, TestQuestion> find = new Finder<Long, TestQuestion>(TestQuestion.class);

}
