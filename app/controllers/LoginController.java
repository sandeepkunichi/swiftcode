package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import data.Channel;
import forms.LoginForm;
import models.AppUser;
import play.Configuration;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import services.AppUserService;
import services.SessionService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
public class LoginController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    @Inject
    AppUserService appUserService;

    @Inject
    WSClient wsClient;

    @Inject
    Configuration configuration;

    @NoAuthRequired
    public Result login() {
        return ok(views.html.shared.login.render(null));
    }

    @NoAuthRequired
    public Result authenticate() throws IOException {
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class).bindFromRequest();
        if(loginForm.hasErrors()){
            return ok(views.html.shared.login.render(loginForm.errorsAsJson()));
        }
        sessionService.storeUserInSession(appUserService.getAppUserByEmail(loginForm.data().get("email")));
        if(sessionService.getSessionUser().role == AppUser.Role.ADMIN)
            return redirect("/admin");
        else
            return redirect("/dashboard");
    }

    @NoAuthRequired
    public Result logout(){
        sessionService.destroySession();
        return redirect("/login");
    }

    @NoAuthRequired
    public Result externalLogin() throws ExecutionException, InterruptedException, IOException {
        AppUser loggedInUser = sessionService.getSessionUser();
        DynamicForm data = formFactory.form().bindFromRequest();
        String code = data.data().get("code");
        String error = data.data().get("error");
        String clientId = configuration.getString("slack.api.client_id");
        String clientSecret = configuration.getString("slack.api.client_secret");
        if(error == null){
            WSRequest request1 = wsClient.url("https://slack.com/api/oauth.access?client_id="+clientId+"&client_secret="+clientSecret+"&code="+code);
            CompletionStage<WSResponse> responsePromise1 = request1.get();
            JsonNode response1 = responsePromise1.thenApply(WSResponse::asJson).toCompletableFuture().get();
            loggedInUser.externalId=response1.get("user_id").asText();
            sessionService.putValue("access_token", response1.get("access_token").asText());
            WSRequest request2 = wsClient.url("https://slack.com/api/channels.list?token="+response1.get("access_token").asText());
            CompletionStage<WSResponse> responsePromise2 = request2.get();
            JsonNode response2 = responsePromise2.thenApply(WSResponse::asJson).toCompletableFuture().get();
            JsonNode channels = response2.get("channels");
            loggedInUser.channels = new ArrayList<>();
            if (channels.isArray()) {
                for (final JsonNode objNode : channels) {
                    if(objNode.get("is_member").asBoolean())
                        loggedInUser.channels.add(new Channel(objNode.get("name").asText(), objNode.get("purpose").get("value").asText(), objNode.get("id").asText()));
                }
            }
            sessionService.saveUserInSession(loggedInUser);
        }
        return redirect("/dashboard");
    }

}