package models.test;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by Sandeep.K on 25-01-2017.
 */
@Entity
public class TestProgram extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(columnDefinition = "TEXT")
    public String programQuestion;

    public TestProgram(String programQuestion){
        this.programQuestion = programQuestion;
    }

    public static Finder<Long, TestProgram> find = new Finder<>(TestProgram.class);

    public String getProgramQuestionView(){
        return utils.HtmlUtils.txtToHtml(programQuestion);
    }
}
