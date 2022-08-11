package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

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
        return filmService.findAll();
    }

    @PostMapping                              // добавление фильма
    public Film addFilm(@RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping                               // обновление фильма
    public Film updateFilm(@RequestBody Film film) {
        return filmService.update(film);
    }

//-----------------------------------------  REST ----------------------------------------------------------------------
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id,
                           @PathVariable Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopTenFilms(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.getTopTenFilms(count);
    }

    @GetMapping("/{id}")                    // получение фильма по ID
    public Film getFilm(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }
}