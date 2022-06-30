package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")

public class FilmController {

    // Loger
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final List<Film> films = new ArrayList<>();


    @GetMapping                                 // получение всех фильмов
    public List<Film> findAll() {
        log.info("Общее количество фильмов: {}", films.size());  // логируем факт получения запроса
        return films;
    }

    @PostMapping                               // добавление фильма
    public Film create(@RequestBody Film film) {
//        Long id = generateId();
//        film.setId(id);
//        listFilms.put(id, film);
//        log.info("Добавлен новый фильм: {}", film);

        if (film.getName().isBlank()) {
            throw new ValidationException("Ошибка ввода! Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Ошибка ввода! максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 27))) {
            throw new ValidationException("Ошибка ввода! дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() > 0) {
            throw new ValidationException("Ошибка ввода! продолжительность фильма должна быть положительной.");
        } else {
            log.info("Объект POST /film сохранен");
            films.add(film);
            return film;
        }
    }

    @PutMapping                             // обновление фильма
    public Film put(@RequestBody Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Ошибка ввода! Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Ошибка ввода! максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 27))) {
            throw new ValidationException("Ошибка ввода! дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() > 0) {
            throw new ValidationException("Ошибка ввода! продолжительность фильма должна быть положительной.");
        } else {
            log.info("Объект POST /film обновлён");
            films.add(film);
            return film;
        }
    }

}
