package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import events.ProgramExecutionEvent;
import models.test.ProgramSubmission;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramExecutionActor extends UntypedActor {

    public static Props props = Props.create(ProgramExecutionActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof ProgramExecutionEvent){

            ProgramExecutionEvent executionEvent = (ProgramExecutionEvent) message;

            ProgramSubmission programSubmission = executionEvent.getProgramSubmission();

            PrintWriter writer = new PrintWriter(executionEvent.getConfiguration().getBinaryFileName(), "UTF-8");
            writer.print(programSubmission.programText);
            writer.close();

            Runtime rt = Runtime.getRuntime();

            String[] commands = {
                    executionEvent.getConfiguration().getCompilerCommand(),
                    executionEvent.getConfiguration().getBinaryFileName()
            };

            Process proc = rt.exec(commands);

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            executionEvent.setOutput(
                    stdError.readLine() == null ? "Compilation Successful" : stdError.lines().collect(Collectors.joining()).replace(executionEvent.getConfiguration().getBinaryRoot(), "")
            );

            sender().tell(executionEvent, self());
        }
    }
}
