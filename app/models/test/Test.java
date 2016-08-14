package models.test;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
@Entity
public class Test extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String title;

    @Constraints.Required
    @OneToMany(cascade = CascadeType.ALL)
    public List<TestQuestion> testQuestions;

    public static Finder<Long, Test> find = new Finder<Long, Test>(Test.class);

}
