package pl.Pakinio.KursUdemy.event;

import pl.Pakinio.KursUdemy.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent {
    TaskUndone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
