package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getAll(); // Получить все фильмы

    Film add(Film film);          // Добавить фильм

    Film update(Film film);       // Обновить фильм

    void remove(Integer id);      // Удалить фильм
}
