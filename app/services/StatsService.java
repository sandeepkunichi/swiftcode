package services;

import data.TestStats;
import data.UserStats;
import models.AppUser;
import models.test.ProgramSubmission;
import models.test.Test;
import models.test.TestSession;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 9/25/2016.
 */
public class StatsService {

    @Inject
    TestSessionService testSessionService;

    @Inject
    AppUserService appUserService;

    public TestStats getTestStats(Long testId){
        TestStats testStats = new TestStats();
        testStats.setActiveSessionCount((long) testSessionService.getActiveTestSessions(testId).size());
        testStats.setSubmittedSessionCount((long) testSessionService.getSubmittedTestSessions(testId).size());
        testStats.setNonTakers(appUserService.getNonTakers(testId));
        testStats.setPendingResumes(appUserService.getPendingResumes(testId));
        testStats.setUnsuccessfulTestSubmitters(appUserService.getUnsuccessfulTakers(testId));
        testStats.setTest(Test.find.byId(testId));
        return testStats;
    }

    public UserStats getUserStats(Long userId){
        UserStats userStats = new UserStats();
        userStats.setAppUser(AppUser.find.byId(userId));
        userStats.setSessions(appUserService.getTestSessionsOfUser(userId));
        return userStats;
    }

    public TestSession getTestSession(Long testSessionId){
        TestSession testSession = TestSession.find.byId(testSessionId);
        testSession.testTaker = AppUser.find.byId(testSession.testTaker.id);
        testSession.programSubmissions = testSession.programSubmissions.stream().map(programSubmission -> ProgramSubmission.find.byId(programSubmission.id)).collect(Collectors.toList());
        return testSession;
    }
}
