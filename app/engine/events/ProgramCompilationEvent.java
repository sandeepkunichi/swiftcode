package engine.events;

import engine.config.ProgramExecutionConfiguration;
import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramCompilationEvent extends ProgramEvent {

    public String output;

    public ProgramCompilationEvent(ProgramSubmission programSubmission, ProgramExecutionConfiguration configuration) {
        super(programSubmission, configuration);
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}
