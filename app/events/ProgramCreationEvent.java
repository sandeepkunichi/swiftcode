package events;

import data.ProgramExecutionConfiguration;
import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 28-01-2017.
 */
public class ProgramCreationEvent {

    private ProgramSubmission programSubmission;
    private ProgramExecutionConfiguration configuration;
    private Boolean creationResult;

    public ProgramCreationEvent(ProgramSubmission programSubmission, ProgramExecutionConfiguration configuration) {
        this.programSubmission = programSubmission;
        this.configuration = configuration;
    }

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

    public Boolean getCreationResult() {
        return creationResult;
    }

    public void setCreationResult(Boolean creationResult) {
        this.creationResult = creationResult;
    }
}
