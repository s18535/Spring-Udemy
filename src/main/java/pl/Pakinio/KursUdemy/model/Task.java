package pl.Pakinio.KursUdemy.model;

import pl.Pakinio.KursUdemy.event.TaskEvent;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //@Column(name = "description")
    @NotBlank(message = "Task's description must be not be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit=new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    public Task() {
    }

    public Task(String description, LocalDateTime deadline){
        this(description,deadline,null);
    }

    public Task(String description, LocalDateTime deadline,TaskGroup group) {
        this.description = description;
        this.deadline = deadline;
        if (group!=null){
            this.group=group;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public TaskEvent toggle() {
        this.done = !this.done;
        return TaskEvent.changed(this);
    }

   /* public void setDone(boolean done) {
        this.done = done;
    }*/

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateForm(final Task source){
        description=source.description;
        done=source.done;
        deadline=source.deadline;
        group=source.group;
    }


}
