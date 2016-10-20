package services;

import data.TestStats;
import data.UserStats;
import models.AppUser;
import models.test.Test;

import javax.inject.Inject;

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
}
