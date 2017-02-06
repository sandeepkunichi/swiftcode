package engine.controllers;

import actions.ValidationAction;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import engine.actors.DispatcherActor;
import engine.config.*;
import engine.events.ProgramCompilationEvent;
import engine.events.ProgramExecutionEvent;
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

    final ActorRef dispatcherActor;
    final Runtime runtime;

    @Inject
    public ProgramController(ActorSystem system) {
        this.dispatcherActor = system.actorOf(DispatcherActor.props);
        this.runtime = Runtime.getRuntime();
    }

    @ValidationAction.ValidationActivity(validationActionType = ProgramSubmission.class)
    public CompletionStage<Result> compile() throws IOException, InterruptedException, ExecutionException {
        Form<ProgramSubmission> programSubmissionForm = formFactory.form(ProgramSubmission.class).bindFromRequest();

        ProgramSubmission programSubmission = programSubmissionForm.get().preProcess();

        ProgramExecutionConfiguration programExecutionConfiguration = getProgramConfiguration(programSubmission);

        ProgramCompilationEvent programCompilationEvent = new ProgramCompilationEvent(
                programSubmission,
                programExecutionConfiguration,
                runtime
        );

        return FutureConverters.toJava(ask(dispatcherActor, programCompilationEvent, 10000))
                .thenApply(this::getExecutionResult);

    }

    @ValidationAction.ValidationActivity(validationActionType = ProgramSubmission.class)
    public CompletionStage<Result> execute() throws IOException, InterruptedException, ExecutionException {
        Form<ProgramSubmission> programSubmissionForm = formFactory.form(ProgramSubmission.class).bindFromRequest();

        ProgramSubmission programSubmission = programSubmissionForm.get().preProcess();

        ProgramExecutionConfiguration programExecutionConfiguration = getProgramConfiguration(programSubmission);

        ProgramExecutionEvent programExecutionEvent = new ProgramExecutionEvent(
                programSubmission,
                programExecutionConfiguration,
                runtime
        );

        return FutureConverters.toJava(ask(dispatcherActor, programExecutionEvent, 60000))
                .thenApply(this::getExecutionResult);

    }

    private Result getExecutionResult(Object object){
        try {
            return (Result) ((CompletableFuture) object).get();
        } catch (InterruptedException | ExecutionException e){
            return ok(programExecutionResponse.getCompilationTimeout());
        }
    }

    private ProgramExecutionConfiguration getProgramConfiguration(ProgramSubmission programSubmission){
        switch (programSubmission.languageType){
            case JAVA:
                return new JavaExecutionConfiguration(configuration.getString("binaryRoot"), programSubmission);
            case PYTHON:
                return new PythonExecutionConfiguration(configuration.getString("binaryRoot"), programSubmission);
            case C:
                return new CProgramConfiguration(configuration.getString("binaryRoot"), programSubmission);
            case CPP:
                return new CPPProgramConfiguration(configuration.getString("binaryRoot"), programSubmission);
            default:
                return null;
        }
    }
}
