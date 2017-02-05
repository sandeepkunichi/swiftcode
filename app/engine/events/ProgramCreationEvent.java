package engine.events;

import engine.config.ProgramExecutionConfiguration;
import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 28-01-2017.
 */
public class ProgramCreationEvent extends ProgramEvent {

    private Boolean creationResult;

    public ProgramCreationEvent(ProgramSubmission programSubmission, ProgramExecutionConfiguration configuration) {
        super(programSubmission, configuration);
    }

    public Boolean getCreationResult() {
        return creationResult;
    }

    public void setCreationResult(Boolean creationResult) {
        this.creationResult = creationResult;
    }
}
