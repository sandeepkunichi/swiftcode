package models.test;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
@Entity
public class TestAnswer extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String answer;

    @ManyToOne
    public TestQuestion testQuestion;

    @Constraints.Required
    @Column(columnDefinition = "CHAR(1) DEFAULT '0'")
    public Boolean isCorrect;

    @Transient
    public Boolean selected;

    public static Finder<Long, TestAnswer> find = new Finder<Long, TestAnswer>(TestAnswer.class);

}
