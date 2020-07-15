package pl.Pakinio.KursUdemy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.Pakinio.KursUdemy.model.Task;
import pl.Pakinio.KursUdemy.model.TaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repo;

    @Test
    void httpGet_returnAllTasks(){
        //given
        int inital = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        //when
        Task[] result=restTemplate.getForObject("http://localhost:"+port+"/tasks",Task[].class);

        //then
        assertThat(result).hasSize(inital+2);
    }

    /*@Test
    void httpPost_savesTask() {
        //given
        Task taskToSave = repo.save(new Task("foo", LocalDateTime.now()));

        //when
        Task task = restTemplate.postForObject("http://localhost:" + port + "/tasks", taskToSave, Task.class);

        //then
        assertThat(task).hasNoNullFieldsOrPropertiesExcept("group");
        assertThat(task).isEqualToComparingOnlyGivenFields(taskToSave, "done", "description", "deadline");
        assertThat(task).isExactlyInstanceOf(Task.class);
    }*/
}