package events;

import data.ProgramExecutionConfiguration;
import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramCompilationEvent {

    private ProgramSubmission programSubmission;
    private ProgramExecutionConfiguration configuration;
    public String output;

    public ProgramCompilationEvent(ProgramSubmission programSubmission, ProgramExecutionConfiguration configuration) {
        this.programSubmission = programSubmission;
        this.configuration = configuration;
    }

    public ProgramSubmission getProgramSubmission() {
        return programSubmission;
    }

    public void setProgramSubmission(ProgramSubmission programSubmission) {
        this.programSubmission = programSubmission;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public ProgramExecutionConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ProgramExecutionConfiguration configuration) {
        this.configuration = configuration;
    }
}
