package controllers.engine;

import actors.ProgramExecutionActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import data.ProgramExecutionConfiguration;
import events.ProgramExecutionEvent;
import models.test.ProgramSubmission;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;
/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    Configuration configuration;

    final ActorRef programExecutionActor;

    @Inject
    public ProgramController(ActorSystem system) {
        this.programExecutionActor = system.actorOf(ProgramExecutionActor.props);
    }

    public CompletionStage<Result> execute() throws IOException, InterruptedException {
        Form<ProgramSubmission> programSubmissionForm = formFactory.form(ProgramSubmission.class).bindFromRequest();
        ProgramSubmission programSubmission = programSubmissionForm.get().preProcess(configuration);
        return FutureConverters
                .toJava(ask(
                        programExecutionActor,
                        new ProgramExecutionEvent(
                                programSubmission,
                                new ProgramExecutionConfiguration(
                                        configuration.getString("binaryRoot"),
                                        programSubmission
                                )
                        ),
                        10000))
                .thenApply(response -> ok(((ProgramExecutionEvent) response).getOutput()));
    }
}
