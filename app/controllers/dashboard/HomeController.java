package controllers.dashboard;

import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.drive.Drive;
import data.DashboardAlert;
import data.Document;
import data.types.DashboardAlertType;
import models.AppUser;
import models.CandidateInformation;
import models.ProfilePicture;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.libs.EventSource;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.*;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
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

    @Inject
    FormFactory formFactory;

    public Result index() throws IOException {
        AppUser loggedInUser = sessionService.getSessionUser();
        loggedInUser.candidateInformation = CandidateInformation.find.byId(loggedInUser.candidateInformation.id);
        return ok(views.html.dashboard.index.render(
                appUserService.getTestSessionsOfUser(loggedInUser.id),
                testService.getAvailableTestsForUser(loggedInUser.id),
                loggedInUser,
                (request().getQueryString("alert") != null && !request().getQueryString("alert").isEmpty()) ? new DashboardAlert(
                        configuration.getString("alerts." + request().getQueryString("alert") + ".message"),
                        configuration.getString("alerts." + request().getQueryString("alert") + ".class")
                ) : null,
                configuration
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

    public Result updateCIF(Long userId) throws ParseException {
        Form<CandidateInformation> candidateInformationForm = formFactory.form(CandidateInformation.class).bindFromRequest();
        CandidateInformation candidateInformation = new CandidateInformation(candidateInformationForm.data());
        candidateInformation.update();
        if(candidateInformation.isComplete()){
            return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.INFORMATION_FORM_COMPLETE));
        }else{
            return redirect("/dashboard?alert="+String.valueOf(DashboardAlertType.INFORMATION_FORM_INCOMPLETE));
        }
    }

    public Result profilePicture(Long userId) throws IOException {
        AppUser appUser = AppUser.find.byId(userId);
        if(appUser != null && appUser.candidateInformation != null){
            ProfilePicture profilePicture = ProfilePicture.find.where().eq("candidate_information_id", appUser.candidateInformation.id).findUnique();
            if(profilePicture != null){
                return ok(profilePicture.fileData).as("image/jpeg");
            }
        }
        return redirect("/images/user.png");
    }

    public Result uploadProfilePic(Long userId) throws IOException {
        AppUser appUser = AppUser.find.byId(userId);
        if(appUser == null){
            return redirect("/dashboard");
        }
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("profilePicture");
        if (picture != null && appUser.candidateInformation != null) {
            ProfilePicture profilePicture = ProfilePicture.find.where().eq("candidate_information_id", appUser.candidateInformation.id).findUnique();
            if(profilePicture == null){
                ProfilePicture profilePictureNew = new ProfilePicture();
                profilePictureNew.candidateInformation = appUser.candidateInformation;
                profilePictureNew.fileData = Files.readAllBytes(picture.getFile().toPath());
                ProfilePicture.db().insert(profilePictureNew);
            }else{
                profilePicture.fileData = Files.readAllBytes(picture.getFile().toPath());
                ProfilePicture.db().update(profilePicture);
            }
        }
        return redirect("/dashboard");
    }

}
