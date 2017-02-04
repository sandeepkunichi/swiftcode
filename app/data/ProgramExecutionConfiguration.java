package data;

import models.AppUser;
import models.test.ProgramSubmission;
import models.test.TestSession;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramExecutionConfiguration {

    private String binaryRoot;
    private String binaryFileName;
    private ProgramSubmission programSubmission;
    private String compilerCommand;
    private String executionCommand;
    private String[] commandOptions;
    private AppUser submitter;
    private String submissionDirectory;
    private String executableFileName;

    public ProgramExecutionConfiguration(String binaryRoot, ProgramSubmission programSubmission) {
        this.binaryRoot = binaryRoot;
        this.programSubmission = programSubmission;
        TestSession testSession = TestSession.find.byId(programSubmission.testSession.id);
        if (testSession != null) {
            this.submitter = AppUser.find.byId(testSession.testTaker.id);
            if (submitter != null) {
                this.submissionDirectory = binaryRoot + String.valueOf(submitter.id + "_" + submitter.email)
                        + "\\" + this.programSubmission.programIndex + "\\";
            }
        }
    }

    public String getBinaryRoot() {
        return binaryRoot;
    }

    public void setBinaryRoot(String binaryRoot) {
        this.binaryRoot = binaryRoot;
    }

    public String getBinaryFileName() {
        switch (programSubmission.languageType){
            case C:
                return submissionDirectory + programSubmission.languageType + programSubmission.programIndex + ".c";
            case JAVA:
                return submissionDirectory + "Main.java";
            case CPP:
                return submissionDirectory + programSubmission.languageType + programSubmission.programIndex + ".cpp";
            case PYTHON:
                return submissionDirectory + programSubmission.languageType + programSubmission.programIndex + ".py";
            default:
                return null;
        }
    }

    public void setBinaryFileName(String binaryFileName) {
        this.binaryFileName = binaryFileName;
    }

    public ProgramSubmission getProgramSubmission() {
        return programSubmission;
    }

    public void setProgramSubmission(ProgramSubmission programSubmission) {
        this.programSubmission = programSubmission;
    }

    public String getCompilerCommand() {
        switch (programSubmission.languageType){
            case C:
                return "gcc";
            case JAVA:
                return "javac";
            case CPP:
                return "g++";
            case PYTHON:
                return "python";
            default:
                return "";
        }
    }

    public void setCompilerCommand(String compilerCommand) {
        this.compilerCommand = compilerCommand;
    }

    public String[] getCommandOptions() {
        switch (programSubmission.languageType) {
            case C:
                return new String[]{"-o", getExecutableFileName()};
            case JAVA:
                return new String[]{};
            case CPP:
                return new String[]{"-o", getExecutableFileName()};
            case PYTHON:
                return new String[]{};
            default:
                return new String[]{};
        }
    }

    public void setCommandOptions(String[] commandOptions) {
        this.commandOptions = commandOptions;
    }

    public AppUser getSubmitter() {
        return submitter;
    }

    public void setSubmitter(AppUser submitter) {
        this.submitter = submitter;
    }

    public String getSubmissionDirectory() {
        return submissionDirectory;
    }

    public void setSubmissionDirectory(String submissionDirectory) {
        this.submissionDirectory = submissionDirectory;
    }

    public String getExecutionCommand() {
        switch (programSubmission.languageType){
            case C:
                return "";
            case JAVA:
                return "java -cp"; // TODO have another method which accepts command line for execution
            case CPP:
                return "";
            case PYTHON:
                return "python";
            default:
                return "";
        }
    }

    public void setExecutionCommand(String executionCommand) {
        this.executionCommand = executionCommand;
    }

    public String getExecutableFileName() {
        switch (programSubmission.languageType) {
            case C:
                return submissionDirectory + programSubmission.languageType + programSubmission.programIndex + ".exe";
            case JAVA:
                return submissionDirectory + "\\ " + returnLastModifiedFile(".class");
            case CPP:
                return submissionDirectory + programSubmission.languageType + programSubmission.programIndex + ".exe";
            case PYTHON:
                return submissionDirectory + programSubmission.languageType + programSubmission.programIndex + ".py";
            default:
                return "";
        }
    }

    public void setExecutableFileName(String executableFileName) {
        this.executableFileName = executableFileName;
    }

    // TODO Ideally, we would want to notify user to compile first and then execute.
    // TODO Also notify users to always compile and then execute.
    // TODO The execute button can be disabled on the UI until compiled?
    private String returnLastModifiedFile(String extension) {
        File[] files = new File(submissionDirectory).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(extension);
            }
        });
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
