package pl.Pakinio.KursUdemy.event;

import pl.Pakinio.KursUdemy.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    public static TaskEvent changed(Task source){
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    private int taskId;
    private Instant occurrence;

    TaskEvent(int taskId, Clock clock) {
        this.taskId = taskId;
        occurrence=Instant.now();
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Instant getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Instant occurrence) {
        this.occurrence = occurrence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"{" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
