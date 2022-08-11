package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

    List<Film> getAll();  // Получить все фильмы

    Optional<Film> getById(Integer id); // Получить фильм по id

    Film add(Film film);          // Добавить фильм

    Film update(Film film);       // Обновить фильм


    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    boolean isFilmExist(Integer id);
}
