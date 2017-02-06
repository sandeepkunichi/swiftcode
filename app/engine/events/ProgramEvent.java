package engine.events;

import engine.config.ProgramExecutionConfiguration;
import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 29-01-2017.
 */
public class ProgramEvent {
    private ProgramSubmission programSubmission;
    private ProgramExecutionConfiguration configuration;
    private Runtime runtime;

    public ProgramEvent(ProgramSubmission programSubmission, ProgramExecutionConfiguration configuration, Runtime runtime) {
        this.setProgramSubmission(programSubmission);
        this.setConfiguration(configuration);
        this.runtime = runtime;
    }

    public ProgramEvent(){

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

    public Runtime getRuntime() {
        return runtime;
    }

    public void setRuntime(Runtime runtime) {
        this.runtime = runtime;
    }
}
