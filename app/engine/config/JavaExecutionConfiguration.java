package engine.config;

import models.test.ProgramSubmission;

import java.io.File;

/**
 * Created by Sandeep.K on 05-02-2017.
 */
public class JavaExecutionConfiguration extends ProgramExecutionConfigurationData implements ProgramExecutionConfiguration {

    public JavaExecutionConfiguration(String binaryRoot, String directoryDelimiter, ProgramSubmission programSubmission) {
        super(binaryRoot, directoryDelimiter, programSubmission);
    }

    @Override
    public String getBinaryFileName() {
        return this.getSubmissionDirectory() + "Main.java";
    }

    @Override
    public String getCompilerCommand() {
        return "javac";
    }

    @Override
    public String[] getCommandOptions() {
        return new String[]{};
    }

    @Override
    public String getExecutionCommand() {
        return "java -cp";
    }

    @Override
    public String getExecutableFileName() {
        //Don't remove space. We don't know why it is needed, but it is needed. :|
        return this.getSubmissionDirectory() + " " + returnLastModifiedFile(".class");
    }

    // TODO Ideally, we would want to notify user to compile first and then execute.
    // TODO Also notify users to always compile and then execute.
    // TODO The execute button can be disabled on the UI until compiled?
    private String returnLastModifiedFile(String extension) {
        File[] files = new File(this.getSubmissionDirectory()).listFiles((dir, name) -> name.endsWith(extension));
        if (files == null || files.length == 0) {
            return "";
        }
        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        String lastModifiedFileName = lastModifiedFile.getName();
        return lastModifiedFileName.substring(0, lastModifiedFileName.lastIndexOf("."));
    }

}
