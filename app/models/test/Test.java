package models.test;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.EnumValue;
import play.data.validation.Constraints;
import utils.HtmlUtils;

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

    @OneToMany(cascade = CascadeType.ALL)
    public List<TestProgram> testPrograms;

    public Long testDuration;

    @Constraints.Required
    @OneToOne
    public CodeSessionConfiguration codeSessionConfiguration;

    @Column(columnDefinition = "TEXT")
    public String instructions;

    @Transient
    public Long testTakerCount;

    public TestStatus testStatus;

    public static Model.Finder<Long, Test> find = new Model.Finder<>(Test.class);

    public String getInstructionsView(){
        return HtmlUtils.txtToHtml(instructions);
    }

    public enum TestStatus {
        @EnumValue("ACTIVE")
        ACTIVE,
        @EnumValue("DRAFT")
        DRAFT
    }
}
