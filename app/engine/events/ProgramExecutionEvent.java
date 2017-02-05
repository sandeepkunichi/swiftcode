package engine.events;

import engine.config.ProgramExecutionConfiguration;
import models.test.ProgramSubmission;
import play.mvc.Result;

import java.io.File;
import java.util.concurrent.CompletionStage;

/**
 * Created by Sandeep.K on 31-01-2017.
 */
public class ProgramExecutionEvent extends ProgramEvent {
    private CompletionStage<Result> output;
    private File executable;

    public ProgramExecutionEvent(ProgramSubmission programSubmission, ProgramExecutionConfiguration configuration) {
        super(programSubmission, configuration);
    }

    public ProgramExecutionEvent(File executable) {
        this.executable = executable;
    }

    public CompletionStage<Result> getOutput() {
        return output;
    }

    public void setOutput(CompletionStage<Result> output) {
        this.output = output;
    }

    public File getExecutable() {
        return executable;
    }

    public void setExecutable(File executable) {
        this.executable = executable;
    }
}
