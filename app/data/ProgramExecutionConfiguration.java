package data;

import models.test.ProgramSubmission;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramExecutionConfiguration {

    private String binaryRoot;
    private String binaryFileName;
    private ProgramSubmission programSubmission;
    private String compilerCommand;

    public ProgramExecutionConfiguration(String binaryRoot, ProgramSubmission programSubmission) {
        this.binaryRoot = binaryRoot;
        this.programSubmission = programSubmission;
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
                return binaryRoot + programSubmission.languageType + programSubmission.id + ".c";
            case JAVA:
                return binaryRoot + programSubmission.languageType + programSubmission.id + ".java";
            case CPP:
                return binaryRoot + programSubmission.languageType + programSubmission.id + ".cpp";
            case PYTHON:
                return binaryRoot + programSubmission.languageType + programSubmission.id + ".py";
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
}
