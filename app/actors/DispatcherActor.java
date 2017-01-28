package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import events.DispatchEvent;
import events.ProgramCreationEvent;
import events.ProgramCompilationEvent;
import scala.compat.java8.FutureConverters;

import static akka.pattern.Patterns.ask;
/**
 * Created by Sandeep.K on 28-01-2017.
 */
public class DispatcherActor extends UntypedActor {

    public static Props props = Props.create(DispatcherActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof DispatchEvent){
            DispatchEvent event = (DispatchEvent) message;
            Boolean creationResult = FutureConverters
                    .toJava(ask(event.getCreationActorRef(), event.getProgramCreationEvent(), 5000))
                    .thenApply(x -> ((ProgramCreationEvent)x).getCreationResult()).toCompletableFuture().get();
            if(creationResult){
                String output = FutureConverters
                        .toJava(ask(event.getCompilationActorRef(), event.getProgramCompilationEvent(), 10000))
                        .thenApply(x -> ((ProgramCompilationEvent)x).getOutput()).toCompletableFuture().get();
                event.setOutput(output);
            }else{
                event.setOutput(views.html.test.code_error.render("Error creating program file", false).body());
            }
            sender().tell(event, self());
        }
    }
}
