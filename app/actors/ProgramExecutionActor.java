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
    private final String LINE_BREAK = "<br />";

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

            executionEvent.setOutput(stdError.readLine() == null ?
                    views.html.test.code_error.render("Compilation Successful", true).body() :
                    views.html.test.code_error.render(stdError.lines()
                            .map(line -> line + LINE_BREAK)
                            .collect(Collectors.joining())
                            .replace(executionEvent.getConfiguration().getBinaryRoot(), ""), false).body()
            );

            sender().tell(executionEvent, self());
        }
    }
}