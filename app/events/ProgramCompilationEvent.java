package events;

import data.ProgramExecutionConfiguration;
import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramCompilationEvent extends ProgramEvent {

    public String output;

    public ProgramCompilationEvent(ProgramSubmission programSubmission, ProgramExecutionConfiguration configuration) {
        this.setProgramSubmission(programSubmission);
        this.setConfiguration(configuration);
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}
