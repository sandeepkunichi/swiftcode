import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.http.HttpErrorHandler;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;


/**
 * Created by Sandeep.K on 9/11/2016.
 */
@Singleton
public class ErrorHandler implements HttpErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        if(statusCode == 404){
            return CompletableFuture.completedFuture(ok(views.html.shared.error.render("The page you are looking for does not exist")));
        }
        return CompletableFuture.completedFuture(ok(views.html.shared.error.render("Something went wrong")));
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        logger.error(Arrays.toString(exception.getStackTrace()));
        exception.printStackTrace();
        return CompletableFuture.completedFuture(ok(views.html.shared.error.render("Something went wrong")));
    }

}
