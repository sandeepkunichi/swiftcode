package controllers.dashboard;

import models.AppUser;
import models.forum.Post;
import models.test.Test;
import models.test.TestSession;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import services.AppUserService;
import services.SessionService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends Controller {

    @Inject
    SessionService sessionService;

    @Inject
    AppUserService appUserService;

    @Inject
    FormFactory formFactory;

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

}
