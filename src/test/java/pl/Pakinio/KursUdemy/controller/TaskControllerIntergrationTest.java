package pl.Pakinio.KursUdemy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.Pakinio.KursUdemy.model.Task;
import pl.Pakinio.KursUdemy.model.TaskRepository;

import java.time.LocalDateTime;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntergrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        //when + then
        mockMvc.perform(get("/tasks/" + id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

   /* @Test
    void httpPost_saveTask() throws Exception {
        //given
        LocalDateTime today = LocalDateTime.now();
        Task exampleTask = new Task("foo", today);
        //Task exampleTask = repo.save(new Task("foo", LocalDateTime.now()));
        //when + then
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleTask))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
                //.andExpect(status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("foo"))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.deadline").value(today))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.done").value(false))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(exampleTask.getId()));
    }*/

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
