import controllers.NoAuthRequired;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by Sandeep.K on 8/15/2016.
 */
public class ActionCreator implements play.http.ActionCreator {

    @Override
    public Action createAction(Http.Request request, Method actionMethod) {

        return new Action.Simple() {
            @Override
            public CompletionStage<Result> call(Http.Context ctx) {
                if (actionMethod.isAnnotationPresent(NoAuthRequired.class) || ctx.session().containsKey("userInSession")) {
                    return delegate.call(ctx);
                } else {
                    return CompletableFuture.completedFuture(redirect("/login"));
                }
            }
        };
    }
}
