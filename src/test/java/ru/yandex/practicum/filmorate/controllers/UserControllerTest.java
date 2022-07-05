package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    UserController userController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createValidUserTest() throws Exception {
        User user = new User(1,"mail@inbox.ru","name", "login",
                LocalDate.of(2022,1,1));
        String json = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateUserInValidEmail() throws Exception {
        User user = new User(1,"emailmail","name", "login",
                LocalDate.of(2022,1,1));
        String json = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateUserInValidName() throws Exception {
        User user = new User(1,"email@inbox.ru","name0123456789name0123456789name0123456789",
                "login",
                LocalDate.of(2022,1,1));
        String json = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateUserInValidLogin() throws Exception {
        User user = new User(1,"email@inbox.ru","name", "name0123456789name0123456789name0123456789",
                LocalDate.of(2022,1,1));
        String json = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldPutUser() throws Exception {
        User user = new User(1,"email@inbox.ru","name", "login", LocalDate.of(2022,1,1));
        User user1 = new User(1,"email@inbox.ru","NAME", "login", LocalDate.of(2022,1,1));
        String json = objectMapper.writeValueAsString(user);
        String json1 = objectMapper.writeValueAsString(user1);
        this.mockMvc.perform(post("/users").content(json).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(put("/users").content(json1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}