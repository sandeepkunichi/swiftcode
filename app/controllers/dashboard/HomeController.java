package controllers.dashboard;

import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.drive.Drive;
import data.Document;
import models.AppUser;
import models.forum.Post;
import models.test.Test;
import models.test.TestSession;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.libs.EventSource;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AppUserService;
import services.DriveService;
import services.MessageService;
import services.SessionService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
public class HomeController extends Controller implements MessageService {

    @Inject
    SessionService sessionService;

    @Inject
    AppUserService appUserService;

    @Inject
    FormFactory formFactory;

    @Inject
    WSClient wsClient;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    Configuration configuration;

    public Result index() throws IOException {
        AppUser loggedInUser = sessionService.getSessionUser();
        List<TestSession> testSessions = appUserService.getTestSessionsOfUser(loggedInUser.id);
        List<Test> tests = Test.find
                .all()
                .stream()
                .filter(test -> !appUserService.hasTakenTest(loggedInUser.id, test.id))
                .collect(Collectors.toList());
        return ok(views.html.dashboard.index.render(testSessions, tests, loggedInUser));
    }

    public Result createPost() throws IOException {
        AppUser loggedInUser = sessionService.getSessionUser();
        Form<Post> postForm = formFactory.form(Post.class).bindFromRequest();
        if(postForm.hasErrors()){
            return ok(postForm.errorsAsJson());
        }else{
            Post post = new Post();
            post.postCreator = loggedInUser;
            post.text = postForm.data().get("text");
            Post.db().insert(post);
        }
        return ok();
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
