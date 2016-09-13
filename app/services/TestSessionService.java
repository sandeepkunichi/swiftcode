package services;

import models.AppUser;
import models.test.Test;
import models.test.TestAnswer;
import models.test.TestSession;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
                            if(testAnswer.selected != null && TestAnswer.find.byId(testAnswer.id).isCorrect && testAnswer.selected){
                                testSession.score++;
                            }
                        }
                )
        );
        return testSession;
    }

    public List<TestSession> findAllTestSessions(){
        return TestSession
                .find
                .all()
                .stream()
                .filter(testSession -> testSession.submitted)
                .map(testSession -> {
                    testSession.test = Test.find.byId(testSession.test.id);
                    testSession.testTaker = AppUser.find.byId(testSession.testTaker.id);
                    testSession.timeTaken = Minutes.minutesBetween(new DateTime(testSession.startTime), new DateTime(testSession.endTime)).getMinutes() % 60;
                    return testSession;
                })
                .sorted((t1, t2) -> Long.compare(t2.score, t1.score))
                .collect(Collectors.toList());
    }

    public Long findTestTakerCount(Long testId){
        return findAllTestSessions().stream().filter(testSession -> Objects.equals(testSession.test.id, testId)).count();
    }
}
