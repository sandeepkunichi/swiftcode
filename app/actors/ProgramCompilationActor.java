package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import events.ProgramCompilationEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramCompilationActor extends UntypedActor {

    public static Props props = Props.create(ProgramCompilationActor.class);
    private final String LINE_BREAK = "<br />";

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof ProgramCompilationEvent){

            ProgramCompilationEvent programCompilationEvent = (ProgramCompilationEvent) message;

            Runtime rt = Runtime.getRuntime();

            String[] commands = {
                    programCompilationEvent.getConfiguration().getCompilerCommand(),
                    programCompilationEvent.getConfiguration().getBinaryFileName()
            };

            Process proc = rt.exec(commands);

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            if(stdError.readLine() == null){
                programCompilationEvent.setOutput(views.html.test.code_error.render("Compilation Successful", true).body());
            } else {
                programCompilationEvent.setOutput(views.html.test.code_error.render(
                        stdError.lines()
                                .map(line -> line + LINE_BREAK)
                                .collect(Collectors.joining())
                                .replace(programCompilationEvent.getConfiguration().getBinaryRoot(), ""), false).body()
                );
            }
            sender().tell(programCompilationEvent, self());
        }
    }
}
