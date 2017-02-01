package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import events.ProgramExecutionEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static play.mvc.Results.ok;

/**
 * Created by Sandeep.K on 31-01-2017.
 */
public class ProgramExecutionActor extends UntypedActor {

    public static Props props = Props.create(ProgramExecutionActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof ProgramExecutionEvent){
            ProgramExecutionEvent programExecutionEvent = (ProgramExecutionEvent) message;

            Runtime rt = Runtime.getRuntime();

            String[] commands = {
                    programExecutionEvent.getConfiguration().getExecutionCommand(),
                    programExecutionEvent.getConfiguration().getExecutableFileName()
            };

            BufferedReader stdOut = null;
            try{
                Process proc = rt.exec(Arrays.asList(commands).stream().collect(Collectors.joining(" ")));

                stdOut = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            } catch(Exception ignored){
                ignored.printStackTrace();
            }

            String output = (stdOut != null && stdOut.lines() != null) ? stdOut.lines().collect(Collectors.joining("<br />")) : "Nothing to show";

            programExecutionEvent.setOutput(CompletableFuture.completedFuture(ok(output)));

            sender().tell(programExecutionEvent, self());
        }
    }
}
