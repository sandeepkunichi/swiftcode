package engine.actors;

import akka.actor.UntypedActor;
import engine.events.ProgramCompilationEvent;
import responses.ProgramExecutionResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public class ProgramCompilationActor extends UntypedActor {

    public ProgramExecutionResponse programExecutionResponse = new ProgramExecutionResponse();

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof ProgramCompilationEvent){

            ProgramCompilationEvent programCompilationEvent = (ProgramCompilationEvent) message;

            String[] commands = {
                    programCompilationEvent.getConfiguration().getCompilerCommand(),
                    Arrays.asList(programCompilationEvent.getConfiguration().getCommandOptions()).stream().collect(Collectors.joining(" ")),
                    programCompilationEvent.getConfiguration().getBinaryFileName()
            };

            Process proc = programCompilationEvent.getRuntime().exec(Arrays.asList(commands).stream().collect(Collectors.joining(" ")));

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
