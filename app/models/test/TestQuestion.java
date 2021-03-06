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
    @Column(columnDefinition = "TEXT")
    public String question;

    @Constraints.Required
    @OneToMany(cascade = CascadeType.ALL)
    public List<TestAnswer> testAnswers;

    public TestQuestion(String question, List<TestAnswer> testAnswers){
        this.question = question;
        this.testAnswers = testAnswers;
    }

    public static Finder<Long, TestQuestion> find = new Finder<>(TestQuestion.class);

    public String getQuestionView(){
        return utils.HtmlUtils.txtToHtml(question);
    }
}
