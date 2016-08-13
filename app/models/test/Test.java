package models.test;

import com.avaje.ebean.Model;
import models.AppUser;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
@Entity
public class Test extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public Date startTime;

    public Date endTime;

    public Long score;

    @Constraints.Required
    @OneToMany(cascade = CascadeType.ALL)
    public List<TestQuestion> testQuestions;

    @ManyToOne
    public AppUser testTaker;

}
