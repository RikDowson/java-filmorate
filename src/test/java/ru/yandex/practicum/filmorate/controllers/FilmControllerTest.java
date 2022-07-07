package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateValidFilmTest() throws Exception {
        Film film = new Film(2, "name", "description",
                LocalDate.of(2022, 1, 12), 123);
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllFilm() throws Exception {
        Film film = new Film(2, "name", "description",
                LocalDate.of(2022, 1, 12), 123);
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(get("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void shouldCreateInValidFilmNameTest() throws Exception {
        Film film = new Film(2, "", "description",
                LocalDate.of(2022, 1, 12), 123);
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateInValidFilmDescriptionTest() throws Exception {
        Film film = new Film(2, "Name", "",
                LocalDate.of(2022, 1, 12), 123);
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateInValidFilmDataTest() throws Exception {
        Film film = new Film(2, "Name", "",
                LocalDate.of(2023, 1, 12), 123);
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldPutFilm() throws Exception {
        Film film = new Film(1, "name", "description", LocalDate.of(2022, 1, 12), 123);
        Film film1 = new Film(1, "name", "description", LocalDate.of(2022, 1, 12), 123);
        String json = objectMapper.writeValueAsString(film);
        String json1 = objectMapper.writeValueAsString(film1);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(put("/films").content(json1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}