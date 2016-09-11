package services;

import models.test.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 9/12/2016.
 */
public class TestService {

    @Inject
    AppUserService appUserService;

    @Inject
    TestSessionService testSessionService;

    public List<Test> findAllTests(){
        return Test.find.all().stream().map(test-> {
            test.testTakerCount = testSessionService.findTestTakerCount(test.id);
            return test;
        }).collect(Collectors.toList());
    }

    public List<Test> getAvailableTestsForUser(Long userId){
        return Test.find.all().stream().filter(test -> !appUserService.hasTakenTest(userId, test.id)).collect(Collectors.toList());
    }

}
