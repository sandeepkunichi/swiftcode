package services;

import models.test.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import models.test.CodeSessionConfiguration;

/**
 * Created by Sandeep.K on 9/12/2016.
 */
public class TestService {

    @Inject
    AppUserService appUserService;

    @Inject
    TestSessionService testSessionService;

    public List<Test> findAllTests(){
        return Test.find.all().stream()
                .map(test-> {
                    test.testTakerCount = testSessionService.findTestTakerCount(test.id);
                    return test;
                }).collect(Collectors.toList());
    }

    public List<Test> getAvailableTestsForUser(Long userId){
        return Test.find.all().stream()
                .filter(test -> !appUserService.hasTakenTest(userId, test.id))
                .filter(test -> test.testStatus == Test.TestStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    public Test cloneTest(Test test){
        Test newTest = new Test();
        newTest.testStatus = Test.TestStatus.DRAFT;
        newTest.title = test.title + " (Clone)";
        newTest.testTakerCount = 0L;
        newTest.instructions = test.instructions;
        newTest.testDuration = test.testDuration;
        newTest.testQuestions = test.testQuestions.stream()
                .map(question ->
                        new TestQuestion(question.question, question.testAnswers
                                .stream()
                                .map(answer ->
                                        new TestAnswer(answer.answer, answer.isCorrect)
                                ).collect(Collectors.toList()))
                ).collect(Collectors.toList());
        newTest.testPrograms = test.testPrograms.stream()
                .map(programmingQuestion -> new TestProgram(programmingQuestion.programQuestion))
                .collect(Collectors.toList());
        test.codeSessionConfiguration = CodeSessionConfiguration.find.byId(test.codeSessionConfiguration.id);
        if (test.codeSessionConfiguration != null) {
            newTest.codeSessionConfiguration = new CodeSessionConfiguration();
            newTest.codeSessionConfiguration.compile = test.codeSessionConfiguration.compile;
            newTest.codeSessionConfiguration.execute = test.codeSessionConfiguration.execute;
            newTest.codeSessionConfiguration.languages = test.codeSessionConfiguration.languages;
            CodeSessionConfiguration.db().insert(newTest.codeSessionConfiguration);
        }
        Test.db().insert(newTest);
        return newTest;
    }

}
