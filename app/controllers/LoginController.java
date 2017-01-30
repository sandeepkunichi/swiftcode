package controllers;

import actions.ValidationAction;
import com.fasterxml.jackson.databind.JsonNode;
import data.Channel;
import data.types.DashboardAlertType;
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
import java.util.Objects;
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
    @ValidationAction.ValidationActivity(validationActionType = LoginForm.class)
    public Result authenticate() throws IOException {
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class).bindFromRequest();
        sessionService.storeUserInSession(appUserService.getAppUserByEmail(loginForm.data().get("email")));
        return sessionService.getSessionUser().isAdmin() ? redirect("/admin") : redirect("/dashboard");
    }

    @NoAuthRequired
    public Result logout(){
        sessionService.destroySession();
        return redirect("/login");
    }

    @NoAuthRequired
    public Result externalLogin() throws ExecutionException, InterruptedException, IOException {
        DynamicForm data = formFactory.form().bindFromRequest();
        String code = data.data().get("code");
        String error = data.data().get("error");
        String userId = data.data().get("state");
        AppUser loggedInUser = appUserService.findById(Long.valueOf(userId));
        String clientId = configuration.getString("slack.api.client_id");
        String clientSecret = configuration.getString("slack.api.client_secret");
        if(error == null){
            WSRequest accessTokenRequest = wsClient.url("https://slack.com/api/oauth.access");
            CompletionStage<WSResponse> responsePromise1 = accessTokenRequest
                    .setQueryParameter("client_id", clientId)
                    .setQueryParameter("client_secret", clientSecret)
                    .setQueryParameter("code", code)
                    .setQueryParameter("redirect_uri", configuration.getString("slack.api.redirect_url"))
                    .get();
            JsonNode accessTokenResponse = responsePromise1.thenApply(WSResponse::asJson).toCompletableFuture().get();

            if(accessTokenResponse.get("ok").asBoolean()){
                loggedInUser.externalId = accessTokenResponse.get("user_id").asText();

                WSRequest teamInfoRequest = wsClient.url("https://slack.com/api/team.info");
                CompletionStage<WSResponse> teamInfoResponsePromise = teamInfoRequest
                        .setQueryParameter("token", accessTokenResponse.get("access_token").asText())
                        .get();
                JsonNode teamInfoResponse = teamInfoResponsePromise.thenApply(WSResponse::asJson).toCompletableFuture().get();

                if(teamInfoResponse.get("ok").asBoolean()){
                    if(!Objects.equals(teamInfoResponse.get("team").get("domain").asText(), configuration.getString("slack.teamId"))){
                        return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.INVALID_TEAM));
                    }
                }else{
                    return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.SLACK_FAILURE));
                }

                sessionService.putValue("access_token", accessTokenResponse.get("access_token").asText());
                WSRequest userDataRequest = wsClient.url("https://slack.com/api/channels.list");
                CompletionStage<WSResponse> responsePromise2 = userDataRequest
                        .setQueryParameter("token", accessTokenResponse.get("access_token").asText())
                        .get();
                JsonNode userDataResponse = responsePromise2.thenApply(WSResponse::asJson).toCompletableFuture().get();
                JsonNode channels = userDataResponse.get("channels");
                loggedInUser.channels = new ArrayList<>();
                if (channels.isArray()) {
                    for (final JsonNode objNode : channels) {
                        if(objNode.get("is_member").asBoolean())
                            loggedInUser.channels.add(new Channel(objNode.get("name").asText(), objNode.get("purpose").get("value").asText(), objNode.get("id").asText()));
                    }
                }
                sessionService.saveUserInSession(loggedInUser);
            }else{
                return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.SLACK_FAILURE));
            }
        }else{
            return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.SLACK_FAILURE));
        }
        return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.SLACK_SUCCESS));
    }

}