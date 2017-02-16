package services;

import com.avaje.ebean.Model;
import models.AppUser;
import models.test.ProgramSubmission;
import models.test.Test;
import models.test.TestAnswer;
import models.test.TestSession;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 9/12/2016.
 */
public class TestSessionService {

    public TestSession createTestSession(Long testId, AppUser testTaker){
        TestSession testSession = new TestSession();
        testSession.startTime = new Date();
        testSession.test = Test.find.byId(testId);
        testSession.testTaker = testTaker;
        testSession.submitted = false;
        TestSession.db().insert(testSession);
        return testSession;
    }

    public TestSession randomizeTestSession(TestSession testSession){
        Collections.shuffle(testSession.test.testQuestions);
        testSession.test.testQuestions.forEach(testQuestion -> Collections.shuffle(testQuestion.testAnswers));
        return testSession;
    }

    public TestSession evaluateTestSession(TestSession testSession){
        testSession.score = 0L;
        testSession.endTime = new Date();
        testSession.submitted = true;
        testSession.test.testQuestions.forEach(
                testQuestion -> testQuestion.testAnswers.forEach(
                        testAnswer -> {
                            TestAnswer testAnswerFromDB = TestAnswer.find.byId(testAnswer.id);
                            Boolean isCorrect = (testAnswerFromDB != null) ? testAnswerFromDB.isCorrect : false;
                            if(testAnswer.selected != null && isCorrect && testAnswer.selected){
                                testSession.score++;
                            }
                        }
                )
        );
        return testSession;
    }

    public List<TestSession> findAllTestSessions(){
        return TestSession.find.all().stream()
                .filter(testSession -> testSession.submitted)
                .map(testSession -> {
                    testSession.test = Test.find.byId(testSession.test.id);
                    testSession.testTaker = AppUser.find.byId(testSession.testTaker.id);
                    testSession.timeTaken = (int)(((testSession.endTime.getTime() - testSession.startTime.getTime()) / 1000) / 60);
                    return testSession;
                })
                .sorted((t1, t2) -> Long.compare(t2.score, t1.score))
                .collect(Collectors.toList());
    }

    public Long findTestTakerCount(Long testId){
        return findAllTestSessions().stream().filter(testSession -> Objects.equals(testSession.test.id, testId)).count();
    }

    public List<TestSession> getActiveTestSessions(Long testId){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Test test = Test.find.byId(testId);
        if(test == null){
            return new ArrayList<>();
        }
        return TestSession.find.all().stream()
                .filter(x -> {
                    Date now = new Date();
                    calendar.setTime(x.startTime);
                    calendar.add(Calendar.MINUTE, Integer.valueOf(test.testDuration.toString()));
                    return !x.submitted && now.before(calendar.getTime()) && Objects.equals(x.test.id, testId);
                }).collect(Collectors.toList());
    }

    public List<TestSession> getSubmittedTestSessions(Long testId){
        return TestSession.find.all().stream()
                .filter(x -> x.submitted && Objects.equals(x.test.id, testId))
                .collect(Collectors.toList());
    }

    public void deleteSession(Long sessionId){
        List<ProgramSubmission> programSubmissions = ProgramSubmission.find.where().eq("test_session_id", sessionId).findList();
        programSubmissions.forEach(Model::delete);
        TestSession testSession = TestSession.find.byId(sessionId);
        if (testSession != null) {
            testSession.delete();
        }
    }
}
