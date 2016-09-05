package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.AppUser;
import play.mvc.Http;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Sandeep.K on 8/15/2016.
 */
public class SessionService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    AppUserService appUserService;

    public void storeUserInSession(AppUser appUser){
        appUser = appUserService.getAppUserByEmail(appUser.email);
        Http.Session session = Http.Context.current().session();
        String userInSession = objectMapper.valueToTree(appUser).toString();
        session.put("userInSession", userInSession);
    }

    public void saveUserInSession(AppUser appUser){
        Http.Session session = Http.Context.current().session();
        String userInSession = objectMapper.valueToTree(appUser).toString();
        session.put("userInSession", userInSession);
    }

    public AppUser getSessionUser() throws IOException {
        Http.Session session = Http.Context.current().session();
        return objectMapper.readValue(session.get("userInSession"), AppUser.class);
    }

    public void destroySession(){
        Http.Context.current().session().clear();
    }

    public String getValue(String key){
        Http.Session session = Http.Context.current().session();
        return session.get(key);
    }

    public void putValue(String key, String value){
        Http.Session session = Http.Context.current().session();
        session.put(key, value);
    }


}
