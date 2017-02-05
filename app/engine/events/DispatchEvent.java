package engine.events;

/**
 * Created by Sandeep.K on 28-01-2017.
 */
public class DispatchEvent {
    private ProgramCreationEvent programCreationEvent;
    private ProgramCompilationEvent programCompilationEvent;
    private ProgramExecutionEvent programExecutionEvent;
    private String output;
    private ActionType actionType;

    public DispatchEvent(ProgramCreationEvent programCreationEvent, ProgramCompilationEvent programCompilationEvent) {
        this.programCreationEvent = programCreationEvent;
        this.programCompilationEvent = programCompilationEvent;
        this.actionType = ActionType.COMPILE;
    }

    public DispatchEvent(ProgramExecutionEvent programExecutionEvent) {
        this.programExecutionEvent = programExecutionEvent;
        this.actionType = ActionType.EXECUTE;
    }

    public ProgramCreationEvent getProgramCreationEvent() {
        return programCreationEvent;
    }

    public void setProgramCreationEvent(ProgramCreationEvent programCreationEvent) {
        this.programCreationEvent = programCreationEvent;
    }

    public ProgramCompilationEvent getProgramCompilationEvent() {
        return programCompilationEvent;
    }

    public void setProgramCompilationEvent(ProgramCompilationEvent programCompilationEvent) {
        this.programCompilationEvent = programCompilationEvent;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public ProgramExecutionEvent getProgramExecutionEvent() {
        return programExecutionEvent;
    }

    public void setProgramExecutionEvent(ProgramExecutionEvent programExecutionEvent) {
        this.programExecutionEvent = programExecutionEvent;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public enum ActionType {
        COMPILE,
        EXECUTE
    }
}
