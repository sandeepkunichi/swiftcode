package services;

import models.AppUser;

/**
 * Created by Sandeep.K on 8/15/2016.
 */
public class AppUserService {

    public AppUser getAppUserByEmail(String email){
        return AppUser.find.where().eq("email", email).findUnique();
    }
}
