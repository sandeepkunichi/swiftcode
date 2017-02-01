package controllers.engine;

import actions.ValidationAction;
import actors.DispatcherActor;
import actors.ProgramCompilationActor;
import actors.ProgramCreationActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import data.ProgramExecutionConfiguration;
import events.DispatchEvent;
import events.ProgramCompilationEvent;
import events.ProgramCreationEvent;
import models.test.ProgramSubmission;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import responses.ProgramExecutionResponse;
import scala.compat.java8.FutureConverters;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static akka.pattern.Patterns.ask;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    Configuration configuration;

    @Inject
    ProgramExecutionResponse programExecutionResponse;

    final ActorRef programCompilationActor;
    final ActorRef programCreationActor;
    final ActorRef dispatcherActor;

    @Inject
    public ProgramController(ActorSystem system) {
        this.programCreationActor = system.actorOf(ProgramCreationActor.props);
        this.programCompilationActor = system.actorOf(ProgramCompilationActor.props);
        this.dispatcherActor = system.actorOf(DispatcherActor.props);
    }

    @ValidationAction.ValidationActivity(validationActionType = ProgramSubmission.class)
    public CompletionStage<Result> execute() throws IOException, InterruptedException, ExecutionException {
        Form<ProgramSubmission> programSubmissionForm = formFactory.form(ProgramSubmission.class).bindFromRequest();

        ProgramSubmission programSubmission = programSubmissionForm.get().preProcess();

        ProgramExecutionConfiguration programExecutionConfiguration = new ProgramExecutionConfiguration(
                configuration.getString("binaryRoot"),
                programSubmission
        );

        ProgramCreationEvent programCreationEvent = new ProgramCreationEvent(
                programSubmission,
                programExecutionConfiguration
        );

        ProgramCompilationEvent programCompilationEvent = new ProgramCompilationEvent(
                programSubmission,
                programExecutionConfiguration
        );

        DispatchEvent dispatchEvent = new DispatchEvent(programCreationEvent, programCompilationEvent, programCreationActor, programCompilationActor);

        return FutureConverters.toJava(ask(dispatcherActor, dispatchEvent, 10000))
                .thenApply(this::getExecutionResult);

    }

    private Result getExecutionResult(Object object){
        try {
            return (Result) ((CompletableFuture) object).get();
        } catch (InterruptedException | ExecutionException e){
            return ok(programExecutionResponse.getCompilationTimeout());
        }
    }
}
