package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import events.DispatchEvent;
import events.ProgramCompilationEvent;
import events.ProgramCreationEvent;
import events.ProgramExecutionEvent;
import play.mvc.Result;
import responses.ProgramExecutionResponse;
import scala.compat.java8.FutureConverters;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;
import static play.mvc.Results.ok;
/**
 * Created by Sandeep.K on 28-01-2017.
 */
public class DispatcherActor extends UntypedActor {

    public static Props props = Props.create(DispatcherActor.class);

    public ProgramExecutionResponse programExecutionResponse = new ProgramExecutionResponse();

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof DispatchEvent){

            DispatchEvent event = (DispatchEvent) message;
            CompletionStage<Result> executionResponse = null;


            if(event.getActionType().equals(DispatchEvent.ActionType.COMPILE)){

                executionResponse = FutureConverters
                        .toJava(ask(event.getCreationActorRef(), event.getProgramCreationEvent(), 5000))
                        //Ask the CreationActor to finish file io and respond with the success status
                        .thenApply(creationResponse -> ((ProgramCreationEvent) creationResponse).getCreationResult())
                        //Ask the CompilationActor to run command on file and respond with the result
                        .thenApply(creationResult -> creationResult ? getCompilationResult(event) : getErrorResult())
                        .toCompletableFuture().get();

            }else if(event.getActionType().equals(DispatchEvent.ActionType.EXECUTE)){

                executionResponse = FutureConverters
                        .toJava(ask(event.getExecutionActorRef(), event.getProgramExecutionEvent(), 10000))
                        //Ask the ExecutionActor to run the command on executable file
                        .thenApply(executionResult -> ((ProgramExecutionEvent) executionResult).getOutput())
                        .toCompletableFuture().get();

            }

            sender().tell(executionResponse, self());
        }
    }

    private CompletionStage<Result> getCompilationResult(DispatchEvent event){
        return FutureConverters.toJava(ask(event.getCompilationActorRef(), event.getProgramCompilationEvent(), 10000))
                .thenApply(x -> ok(((ProgramCompilationEvent)x).getOutput()));
    }

    private CompletionStage<Result> getErrorResult(){
        return CompletableFuture.completedFuture(ok(programExecutionResponse.getErrorCreatingProgramFile()));
    }
}
