package data;

import models.AppUser;
import models.test.Test;

import java.util.List;

/**
 * Created by Sandeep.K on 9/25/2016.
 */
public class TestStats {
    private Long activeSessionCount;
    private Long submittedSessionCount;
    private Test test;
    private List<AppUser> unsuccessfulTestSubmitters;
    private List<AppUser> pendingResumes;
    private List<AppUser> nonTakers;

    public Long getActiveSessionCount() {
        return activeSessionCount;
    }

    public void setActiveSessionCount(Long activeSessionCount) {
        this.activeSessionCount = activeSessionCount;
    }

    public Long getSubmittedSessionCount() {
        return submittedSessionCount;
    }

    public void setSubmittedSessionCount(Long submittedSessionCount) {
        this.submittedSessionCount = submittedSessionCount;
    }

    public List<AppUser> getUnsuccessfulTestSubmitters() {
        return unsuccessfulTestSubmitters;
    }

    public void setUnsuccessfulTestSubmitters(List<AppUser> unsuccessfullTestSubmitters) {
        this.unsuccessfulTestSubmitters = unsuccessfullTestSubmitters;
    }

    public List<AppUser> getPendingResumes() {
        return pendingResumes;
    }

    public void setPendingResumes(List<AppUser> pendingResumes) {
        this.pendingResumes = pendingResumes;
    }

    public List<AppUser> getNonTakers() {
        return nonTakers;
    }

    public void setNonTakers(List<AppUser> nonTakers) {
        this.nonTakers = nonTakers;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
