package models.test;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    public TestQuestion testQuestion;

    @Constraints.Required
    @Column(columnDefinition = "CHAR(1) DEFAULT '0'")
    public Boolean isCorrect;

    @Transient
    public Boolean selected;

    public TestAnswer(String answer, Boolean isCorrect){
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public static Finder<Long, TestAnswer> find = new Finder<>(TestAnswer.class);

}
