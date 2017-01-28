package events;

import akka.actor.ActorRef;

/**
 * Created by Sandeep.K on 28-01-2017.
 */
public class DispatchEvent {
    private ProgramCreationEvent programCreationEvent;
    private ProgramCompilationEvent programCompilationEvent;
    private String output;
    private ActorRef creationActorRef;
    private ActorRef compilationActorRef;

    public DispatchEvent(ProgramCreationEvent programCreationEvent, ProgramCompilationEvent programCompilationEvent, ActorRef creationActorRef, ActorRef compilationActorRef) {
        this.programCreationEvent = programCreationEvent;
        this.programCompilationEvent = programCompilationEvent;
        this.creationActorRef = creationActorRef;
        this.compilationActorRef = compilationActorRef;
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
}
