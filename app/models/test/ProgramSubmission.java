package models.test;

import com.avaje.ebean.Model;
import data.types.LanguageType;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by Sandeep.K on 25-01-2017.
 */
@Entity
public class ProgramSubmission extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    public TestProgram testProgram;

    @ManyToOne
    public TestSession testSession;

    @Constraints.Required
    @Column(columnDefinition = "TEXT")
    public String programText;

    @Constraints.Required
    public LanguageType languageType;

    @Transient
    public String programIndex;

    public String getProgramTextView(){
        return this.programText;
    }

    public String getProgramQuestion(){
        return this.testProgram.getProgramQuestionView();
    }

    public static Finder<Long, ProgramSubmission> find = new Finder<>(ProgramSubmission.class);

    // TODO Ideally, we would put the default template in the editor for the user, so we don't require this
    public ProgramSubmission preProcess(){
        if(this.languageType.equals(LanguageType.JAVA)){
            this.programText = this.programText.trim().replaceAll("\u200B|\u200Cc|\u200Dd|\uFEFFe", "");
        }
        return this;
    }
}
