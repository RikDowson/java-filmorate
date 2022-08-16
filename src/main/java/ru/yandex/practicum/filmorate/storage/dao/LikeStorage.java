package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;

public interface LikeStorage {

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

}
