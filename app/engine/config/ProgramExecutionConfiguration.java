package engine.config;

/**
 * Created by Sandeep.K on 05-02-2017.
 */
public interface ProgramExecutionConfiguration {
    String getBinaryFileName();
    String getCompilerCommand();
    String[] getCommandOptions();
    String getExecutionCommand();
    String getExecutableFileName();
    String getSubmissionDirectory();
    String getBinaryRoot();
}
