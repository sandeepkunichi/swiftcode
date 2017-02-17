package engine.actors;

import akka.actor.*;
import akka.remote.routing.RemoteRouterConfig;
import akka.routing.RoundRobinPool;
import engine.events.ProgramCompilationEvent;
import engine.events.ProgramCreationEvent;
import engine.events.ProgramExecutionEvent;
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

    Address[] creationRouterAddresses = {new Address("akka.tcp", "localhost", "127.0.0.1", 9001), AddressFromURIString.parse("akka.tcp://localhost@127.0.0.1:9001")};
    ActorRef creationRouter = getContext().actorOf(new RemoteRouterConfig(new RoundRobinPool(5), creationRouterAddresses).props(Props.create(ProgramCreationActor.class)));

    Address[] compilationRouterAddresses = {new Address("akka.tcp", "localhost", "127.0.0.1", 9002), AddressFromURIString.parse("akka.tcp://localhost@127.0.0.1:9002")};
    ActorRef compilationRouter = getContext().actorOf(new RemoteRouterConfig(new RoundRobinPool(5), compilationRouterAddresses).props(Props.create(ProgramCompilationActor.class)));

    Address[] executionRouterAddresses = {new Address("akka.tcp", "localhost", "127.0.0.1", 9003), AddressFromURIString.parse("akka.tcp://localhost@127.0.0.1:9003")};
    ActorRef executionRouter = getContext().actorOf(new RemoteRouterConfig(new RoundRobinPool(5), executionRouterAddresses).props(Props.create(ProgramExecutionActor.class)));

    @Override
    public void onReceive(Object message) throws Exception {
        CompletionStage<Result> executionResponse = null;

        if(message instanceof ProgramCompilationEvent){
            ProgramCreationEvent programCreationEvent = new ProgramCreationEvent(
                    ((ProgramCompilationEvent) message).getProgramSubmission(),
                    ((ProgramCompilationEvent) message).getConfiguration(),
                    ((ProgramCompilationEvent) message).getRuntime()
            );

            executionResponse = FutureConverters
                    .toJava(ask(creationRouter, programCreationEvent, 5000))
                    //Ask the CreationActor to finish file io and respond with the success status
                    .thenApply(creationResponse -> ((ProgramCreationEvent) creationResponse).getCreationResult())
                    //Ask the CompilationActor to run command on file and respond with the result
                    .thenApply(creationResult -> creationResult ? getCompilationResult((ProgramCompilationEvent) message) : getErrorResult())
                    .toCompletableFuture().get();

        }else if(message instanceof ProgramExecutionEvent){
            executionResponse = FutureConverters
                    .toJava(ask(executionRouter, message, 10000))
                    //Ask the ExecutionActor to run the command on executable file
                    .thenApply(executionResult -> ((ProgramExecutionEvent) executionResult).getOutput())
                    .toCompletableFuture().get();
        }

        sender().tell(executionResponse, self());
    }

    private CompletionStage<Result> getCompilationResult(ProgramCompilationEvent event){
        return FutureConverters.toJava(ask(compilationRouter, event, 10000))
                .thenApply(x -> ok(((ProgramCompilationEvent)x).getOutput()));
    }
    private CompletionStage<Result> getErrorResult(){
        return CompletableFuture.completedFuture(ok(programExecutionResponse.getErrorCreatingProgramFile()));
    }
}
