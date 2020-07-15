package pl.Pakinio.KursUdemy.model.projection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.Pakinio.KursUdemy.model.Task;
import pl.Pakinio.KursUdemy.model.TaskGroup;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline() {
    //given
        TaskGroup source=new TaskGroup();
        source.setDescription("foo");

        Set<Task> set=new HashSet<>();
        set.add(new Task("bar",null));


        source.setTasks(set);

        //when
        GroupReadModel result=new GroupReadModel(source);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline",null);

    }

}