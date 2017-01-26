package models.test;

import com.avaje.ebean.Model;
import data.types.LanguageType;
import play.Configuration;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.text.MessageFormat;

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

    public static Finder<Long, ProgramSubmission> find = new Finder<>(ProgramSubmission.class);

    public ProgramSubmission preProcess(Configuration configuration){
        if(this.languageType.equals(LanguageType.JAVA)){
            this.programText = MessageFormat.format(
                    configuration.getString("programPreProcessing." + this.languageType),
                    this.id.toString(), this.programText
            );
        }
        return this;
    }
}
