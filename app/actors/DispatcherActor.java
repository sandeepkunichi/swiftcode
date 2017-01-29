package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import events.DispatchEvent;
import events.ProgramCreationEvent;
import events.ProgramCompilationEvent;
import play.mvc.Result;
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

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof DispatchEvent){
            DispatchEvent event = (DispatchEvent) message;
            CompletionStage<Result> response;
            Boolean creationResult = FutureConverters
                    .toJava(ask(event.getCreationActorRef(), event.getProgramCreationEvent(), 5000))
                    .thenApply(x -> ((ProgramCreationEvent)x).getCreationResult()).toCompletableFuture().get();

            response = creationResult ?
                    FutureConverters
                            .toJava(ask(event.getCompilationActorRef(), event.getProgramCompilationEvent(), 10000))
                            .thenApply(x -> ok(((ProgramCompilationEvent)x).getOutput()))
                    :
                    CompletableFuture
                            .completedFuture(ok(views.html.test.code_error.render("Error creating program file", false).body()));

            sender().tell(response, self());
        }
    }
}
