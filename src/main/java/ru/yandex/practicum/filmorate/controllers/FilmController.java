package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    // Loger
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    protected Integer countId = 1;

    public int generateFilmId() {
        return countId++;
    }

    @GetMapping                                 // получение всех фильмов
    public Collection<Film> findAll()  throws ValidationException {
        log.debug("Общее количество фильмов: {}", films.size());  // логируем факт получения запроса
        return films.values();
    }

    @PostMapping                               // добавление фильма
    public Film create(@Valid @RequestBody Film film)  throws ValidationException {
        int id = generateFilmId();
        film.setId(id);
        validatorFilm(film);
        log.debug("Добавлен новый фильм: {}{}", id, film.getName());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping                             // обновление фильма
    public Film put(@Valid @RequestBody Film film)  throws ValidationException {
        validatorFilm(film);
        log.debug("Объект POST /film обновлён");
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Нет такого фильма");
        }
        return film;
    }

    public void validatorFilm (Film filmVal) {
        if (filmVal.getName() == null || filmVal.getName().isBlank()) {
            log.debug("Название фильма не может быть пустым");
            throw new ValidationException("Ошибка ввода! Название фильма не может быть пустым.");
        }
        if (filmVal.getDescription().length() > 200) {
            log.debug("Максимальная длина описания — 200 символов");
            throw new ValidationException("Ошибка ввода! Максимальная длина описания — 200 символов");
        }
        if (filmVal.getReleaseDate().isBefore(LocalDate.of(1895, 12, 27))) {
            log.debug("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Ошибка ввода! Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (filmVal.getDuration() <= 0) {
            log.debug("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Ошибка ввода! продолжительность фильма должна быть положительной.");
        }
    }
}