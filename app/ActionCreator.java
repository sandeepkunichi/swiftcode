import com.google.inject.Inject;
import controllers.NoAuthRequired;
import controllers.admin.AdminOnly;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.SessionService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.redirect;

/**
 * Created by Sandeep.K on 8/15/2016.
 */
public class ActionCreator implements play.http.ActionCreator {

    @Inject
    SessionService sessionService;

    @Override
    public Action createAction(Http.Request request, Method actionMethod) {

        return new Action.Simple() {
            @Override
            public CompletionStage<Result> call(Http.Context ctx) {
                if (actionMethod.isAnnotationPresent(NoAuthRequired.class)) {
                    return delegate.call(ctx);
                } else if (ctx.session().containsKey("userInSession")) {
                    try {
                        if(actionMethod.isAnnotationPresent(AdminOnly.class) && !sessionService.getSessionUser().isAdmin()){
                            return logout();
                        }
                    } catch (IOException e) {
                        return logout();
                    }
                    return delegate.call(ctx);
                } else {
                    return logout();
                }
            }
        };
    }

    public CompletableFuture<Result> logout(){
        sessionService.destroySession();
        return CompletableFuture.completedFuture(redirect("/login"));
    }
}
