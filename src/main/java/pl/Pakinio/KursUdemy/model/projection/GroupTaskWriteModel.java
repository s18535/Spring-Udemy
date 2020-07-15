package pl.Pakinio.KursUdemy.model.projection;

import org.springframework.format.annotation.DateTimeFormat;
import pl.Pakinio.KursUdemy.model.Task;
import pl.Pakinio.KursUdemy.model.TaskGroup;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class GroupTaskWriteModel {
    @NotBlank(message = "Task's description must be not be empty")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask(TaskGroup group){
        return new Task(description,deadline,group);
    }
}
