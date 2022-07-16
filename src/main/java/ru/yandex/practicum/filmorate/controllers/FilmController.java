package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired         // Автоматически внедряем зависимость filmService
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping                               // получение всех фильмов
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms().values();
    }

    @PostMapping                              // добавление фильма
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping                               // обновление фильма
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

//----------------------------------------------------------------------------------------------------------------------
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    public Collection<Film> getTopTenFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopTenFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }
}