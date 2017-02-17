package engine.config;

import models.AppUser;
import models.test.ProgramSubmission;
import models.test.TestSession;

/**
 * Created by Sandeep.K on 05-02-2017.
 */
public class ProgramExecutionConfigurationData {

    private String binaryRoot;
    private String directoryDelimiter;
    private ProgramSubmission programSubmission;
    private AppUser submitter;
    private String submissionDirectory;

    public ProgramExecutionConfigurationData(String binaryRoot, String directoryDelimiter, ProgramSubmission programSubmission){
        this.setBinaryRoot(binaryRoot);
        this.directoryDelimiter = directoryDelimiter;
        this.setProgramSubmission(programSubmission);
        TestSession testSession = TestSession.find.byId(programSubmission.testSession.id);
        if (testSession != null) {
            this.setSubmitter(AppUser.find.byId(testSession.testTaker.id));
            if (this.getSubmitter() != null) {
                this.setSubmissionDirectory(
                        binaryRoot +
                                String.valueOf(this.getSubmitter().id + "_" + this.getSubmitter().email) +
                                "\\" +
                                this.getProgramSubmission().programIndex +
                                "\\"
                );
            }
        }
    }

    public String getBinaryRoot() {
        return binaryRoot;
    }

    public void setBinaryRoot(String binaryRoot) {
        this.binaryRoot = binaryRoot;
    }

    public String getDirectoryDelimiter() {
        return directoryDelimiter;
    }

    public void setDirectoryDelimiter(String directoryDelimiter) {
        this.directoryDelimiter = directoryDelimiter;
    }

    public ProgramSubmission getProgramSubmission() {
        return programSubmission;
    }

    public void setProgramSubmission(ProgramSubmission programSubmission) {
        this.programSubmission = programSubmission;
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
}
