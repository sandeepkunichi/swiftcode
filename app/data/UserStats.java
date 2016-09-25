package data;

import models.AppUser;
import models.test.TestSession;

import java.util.List;

/**
 * Created by Sandeep.K on 9/25/2016.
 */
public class UserStats {
    private AppUser appUser;
    private List<TestSession> sessions;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public List<TestSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<TestSession> sessions) {
        this.sessions = sessions;
    }
}
