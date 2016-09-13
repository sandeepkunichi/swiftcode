package controllers.dashboard;

import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.drive.Drive;
import data.DashboardAlert;
import data.Document;
import models.AppUser;
import play.Configuration;
import play.libs.EventSource;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
public class HomeController extends Controller implements MessageService {

    @Inject
    SessionService sessionService;

    @Inject
    AppUserService appUserService;

    @Inject
    WSClient wsClient;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    Configuration configuration;

    @Inject
    TestService testService;

    public Result index() throws IOException {
        AppUser loggedInUser = sessionService.getSessionUser();
        return ok(views.html.dashboard.index.render(
                appUserService.getTestSessionsOfUser(loggedInUser.id),
                testService.getAvailableTestsForUser(loggedInUser.id),
                loggedInUser,
                (request().getQueryString("alert") != null && !request().getQueryString("alert").isEmpty()) ? new DashboardAlert(
                        configuration.getString("alerts." + request().getQueryString("alert") + ".message"),
                        configuration.getString("alerts." + request().getQueryString("alert") + ".class")
                ) : null
        ));
    }

    public Result slackRedirect() throws IOException {
        AppUser loggedInUser = sessionService.getSessionUser();
        return redirect("https://slack.com/oauth/authorize?" +
                "scope=" + configuration.getString("slack.api.scope") + "&" +
                "client_id=" + configuration.getString("slack.api.client_id") + "&" +
                "state=" + loggedInUser.id + "&" +
                "redirect_uri=" + configuration.getString("slack.api.redirect_url"));
    }

    public Result getMessages(String channelId) throws ExecutionException, InterruptedException, IOException {
        String token = sessionService.getValue("access_token");
        final Source<EventSource.Event, ?> eventSource = getMessageSource(token, channelId, wsClient, objectMapper).map(x->EventSource.Event.event(x.body()));
        return ok().chunked(eventSource.via(EventSource.flow())).as(Http.MimeTypes.EVENT_STREAM);
    }

    public Result uploadResume() throws IOException {
        AppUser loggedInUser = sessionService.getSessionUser();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart document = body.getFile("resume");
        Drive service = DriveService.getDriveService();
        java.io.File file = (java.io.File)document.getFile();
        String uploadDirectory = configuration.getString("drive.resume.directory");
        DriveService.insertFile(new Document(service, loggedInUser.email, loggedInUser.email+"'s resume", uploadDirectory, "application/pdf", file));
        loggedInUser.resumeSubmitted=true;
        loggedInUser.update();
        sessionService.saveUserInSession(loggedInUser);
        return ok();
    }

}
