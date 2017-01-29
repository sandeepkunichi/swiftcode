package events;

import data.ProgramExecutionConfiguration;
import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 29-01-2017.
 */
public class ProgramEvent {
    private ProgramSubmission programSubmission;
    private ProgramExecutionConfiguration configuration;

    public ProgramSubmission getProgramSubmission() {
        return programSubmission;
    }

    public void setProgramSubmission(ProgramSubmission programSubmission) {
        this.programSubmission = programSubmission;
    }

    public ProgramExecutionConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ProgramExecutionConfiguration configuration) {
        this.configuration = configuration;
    }
}
