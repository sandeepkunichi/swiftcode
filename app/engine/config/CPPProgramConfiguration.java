package engine.config;

import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 05-02-2017.
 */
public class CPPProgramConfiguration extends ProgramExecutionConfigurationData implements ProgramExecutionConfiguration {

    public CPPProgramConfiguration(String binaryRoot, String directoryDelimiter, ProgramSubmission programSubmission){
        super(binaryRoot, directoryDelimiter, programSubmission);
    }

    @Override
    public String getBinaryFileName() {
        return this.getSubmissionDirectory() +
                this.getProgramSubmission().languageType +
                this.getProgramSubmission().programIndex +
                ".cpp";
    }

    @Override
    public String getCompilerCommand() {
        return "g++";
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
