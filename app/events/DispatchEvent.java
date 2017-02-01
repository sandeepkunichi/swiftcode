package events;

import akka.actor.ActorRef;

/**
 * Created by Sandeep.K on 28-01-2017.
 */
public class DispatchEvent {
    private ProgramCreationEvent programCreationEvent;
    private ProgramCompilationEvent programCompilationEvent;
    private ProgramExecutionEvent programExecutionEvent;
    private String output;
    private ActorRef creationActorRef;
    private ActorRef compilationActorRef;
    private ActorRef executionActorRef;
    private ActionType actionType;

    public DispatchEvent(ProgramCreationEvent programCreationEvent, ProgramCompilationEvent programCompilationEvent, ActorRef creationActorRef, ActorRef compilationActorRef) {
        this.programCreationEvent = programCreationEvent;
        this.programCompilationEvent = programCompilationEvent;
        this.creationActorRef = creationActorRef;
        this.compilationActorRef = compilationActorRef;
        this.actionType = ActionType.COMPILE;
    }

    public DispatchEvent(ProgramExecutionEvent programExecutionEvent, ActorRef executionActorRef) {
        this.programExecutionEvent = programExecutionEvent;
        this.executionActorRef = executionActorRef;
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

    public ActorRef getCreationActorRef() {
        return creationActorRef;
    }

    public void setCreationActorRef(ActorRef creationActorRef) {
        this.creationActorRef = creationActorRef;
    }

    public ActorRef getCompilationActorRef() {
        return compilationActorRef;
    }

    public void setCompilationActorRef(ActorRef compilationActorRef) {
        this.compilationActorRef = compilationActorRef;
    }

    public ProgramExecutionEvent getProgramExecutionEvent() {
        return programExecutionEvent;
    }

    public void setProgramExecutionEvent(ProgramExecutionEvent programExecutionEvent) {
        this.programExecutionEvent = programExecutionEvent;
    }

    public ActorRef getExecutionActorRef() {
        return executionActorRef;
    }

    public void setExecutionActorRef(ActorRef executionActorRef) {
        this.executionActorRef = executionActorRef;
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
