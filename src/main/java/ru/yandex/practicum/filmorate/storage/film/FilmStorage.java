package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getAllFilms(); // Получить все фильмы

    Film addFilm(Film film);          // Добавить фильм

    Film updateFilm(Film film);       // Обновить фильм

    void removeFilm(Integer id);      // Удалить фильм
}
