package io.github.GreenMushroomer03.todoapp.controller;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.GreenMushroomer03.todoapp.model.Task;
import io.github.GreenMushroomer03.todoapp.model.TaskRepository;
import org.assertj.core.api.ObjectAssert;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTestE2ETest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repo;



    @Test
    void httpGet_returnsAllTasks() {
        // given
        int initial = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));


        // when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        // then
        assertThat(result).hasSize(initial+ 2);

    }

    @Test
    void httpGet_returnsTaskForId() {
        // given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);

        // then
        assertThat(result).isInstanceOf(Task.class);
        assertThat(result.getDescription()).isEqualTo("foo");
    }

    @Test
    void httpPost_returnsSavedTask() {
        // given
        String url = "http://localhost:" + port + "/tasks";
        // and
        var taskJsonObject = getJsonObject(false, "JsonTest", LocalDateTime.now());
        // and
        HttpEntity<String> httpEntity = getHttpEntity(taskJsonObject);
        // when
        Task result = restTemplate.postForObject(url, httpEntity, Task.class);

        // then
        assertThat(result).isInstanceOf(Task.class);
        assertThat(result.getDescription()).isEqualTo("JsonTest");

    }

    @Test
    void httpPut_returnsNoContent() {
        // given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();
        // and
        String url = "http://localhost:" + port + "/tasks/" + id;
        // and
        JSONObject taskJsonObject = getJsonObject(true, "Put", LocalDateTime.now());
        HttpEntity<String> httpEntity = getHttpEntity(taskJsonObject);
        // when
        restTemplate.put(url, httpEntity, Task.class);
        Task result = repo.findById(id).orElse(null);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Put");
        assertThat(result.isDone()).isTrue();
    }

    private static JSONObject getJsonObject(boolean done, String description, LocalDateTime dateTime) {
        var taskJsonObject = new JSONObject();
        try {
            taskJsonObject.put("description", description);
            taskJsonObject.put("done", done);
            taskJsonObject.put("deadline", dateTime.toString());
        } catch (JSONException e) {
           throw new RuntimeException(e);
        }
        return taskJsonObject;
    }

    private static HttpEntity<String> getHttpEntity(JSONObject taskJsonObject) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(taskJsonObject.toString(), headers);
    }
}