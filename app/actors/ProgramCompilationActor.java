package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import events.ProgramCompilationEvent;
import responses.ProgramExecutionResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramCompilationActor extends UntypedActor {

    public static Props props = Props.create(ProgramCompilationActor.class);

    public ProgramExecutionResponse programExecutionResponse = new ProgramExecutionResponse();

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
                programCompilationEvent.setOutput(programExecutionResponse.getCompilationSuccessful());
            } else {
                programCompilationEvent.setOutput(programExecutionResponse.getCompilationError(stdError, programCompilationEvent));
            }
            sender().tell(programCompilationEvent, self());
        }
    }
}
