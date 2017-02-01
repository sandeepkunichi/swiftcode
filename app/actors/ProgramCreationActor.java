package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import events.ProgramCreationEvent;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Sandeep.K on 28-01-2017.
 */
public class ProgramCreationActor extends UntypedActor {
    public static Props props = Props.create(ProgramCreationActor.class);
    @Override
    public void onReceive(Object message) {
        if(message instanceof ProgramCreationEvent){
            ProgramCreationEvent event = (ProgramCreationEvent) message;
            try{
                Files.createDirectories(Paths.get(event.getConfiguration().getSubmissionDirectory()));
                PrintWriter writer = new PrintWriter(event.getConfiguration().getBinaryFileName(), "UTF-8");
                writer.print(event.getProgramSubmission().programText);
                writer.close();
                event.setCreationResult(true);
            }catch (Exception ex){
                event.setCreationResult(false);
            }
            sender().tell(event, self());
        }
    }
}
