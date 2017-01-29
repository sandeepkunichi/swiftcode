package responses;

import events.ProgramCompilationEvent;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 29-01-2017.
 */
@Singleton
public class ProgramExecutionResponse {

    private final String LINE_BREAK = "<br />";

    public String getErrorCreatingProgramFile(){
        return views.html.test.code_error.render("Error creating program file", false).body();
    }

    public String getCompilationSuccessful(){
        return views.html.test.code_error.render("Compilation Successful", true).body();
    }

    public String getCompilationError(BufferedReader stdError, ProgramCompilationEvent programCompilationEvent){
        return views.html.test.code_error.render(stdError.lines()
                .map(line -> line + LINE_BREAK)
                .collect(Collectors.joining())
                .replace(programCompilationEvent.getConfiguration().getBinaryRoot(), ""), false).body();
    }

    public String getCompilationTimeout(){
        return views.html.test.code_error.render("Your program took too long to execute", false).body();
    }
}
