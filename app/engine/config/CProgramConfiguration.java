package engine.config;

import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 05-02-2017.
 */
public class CProgramConfiguration extends ProgramExecutionConfigurationData implements ProgramExecutionConfiguration {

    public CProgramConfiguration(String binaryRoot, ProgramSubmission programSubmission){
        super(binaryRoot, programSubmission);
    }

    @Override
    public String getBinaryFileName() {
        return this.getSubmissionDirectory() +
                this.getProgramSubmission().languageType +
                this.getProgramSubmission().programIndex +
                ".c";
    }

    @Override
    public String getCompilerCommand() {
        return "gcc";
    }

    @Override
    public String[] getCommandOptions() {
        return new String[]{"-o", getExecutableFileName()};
    }

    @Override
    public String getExecutionCommand() {
        return "";
    }

    @Override
    public String getExecutableFileName() {
        return this.getSubmissionDirectory() +
                this.getProgramSubmission().languageType +
                this.getProgramSubmission().programIndex +
                ".exe";
    }
}
