package pl.Pakinio.KursUdemy.event;

import pl.Pakinio.KursUdemy.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
