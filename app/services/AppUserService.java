package services;

import models.AppUser;
import models.test.Test;
import models.test.TestSession;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 8/15/2016.
 */
public class AppUserService {

    public List<AppUser> findAllAppUsers(){
        return AppUser.find.all();
    }

    public AppUser getAppUserByEmail(String email){
        return AppUser.find.where().eq("email", email).findUnique();
    }

    public List<TestSession> getTestSessionsOfUser(Long userId){
        return TestSession.find.where().eq("test_taker_id", userId).findList().stream().map(testSession -> {
            testSession.test = Test.find.byId(testSession.test.id);
            return testSession;
        }).collect(Collectors.toList());
    }

    public AppUser findById(Long userId){
        return AppUser.find.byId(userId);
    }

    public Boolean hasTakenTest(Long userId, Long testId){
        return getTestSessionsOfUser(userId)
                .stream()
                .anyMatch(testSession -> Objects.equals(testSession.test.id, testId));
    }
}
