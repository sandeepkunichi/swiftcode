package engine.config;

import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 05-02-2017.
 */
public class PythonExecutionConfiguration extends ProgramExecutionConfigurationData implements ProgramExecutionConfiguration {

    public PythonExecutionConfiguration(String binaryRoot, ProgramSubmission programSubmission) {
        super(binaryRoot, programSubmission);
    }

    @Override
    public String getBinaryFileName() {
        return this.getSubmissionDirectory() +
                this.getProgramSubmission().languageType +
                this.getProgramSubmission().programIndex +
                ".py";
    }

    @Override
    public String getCompilerCommand() {
        return "python";
    }

    @Override
    public String[] getCommandOptions() {
        return new String[]{};
    }

    @Override
    public String getExecutionCommand() {
        return "python";
    }

    @Override
    public String getExecutableFileName() {
        return this.getSubmissionDirectory() +
                this.getProgramSubmission().languageType +
                this.getProgramSubmission().programIndex +
                ".py";
    }
}
