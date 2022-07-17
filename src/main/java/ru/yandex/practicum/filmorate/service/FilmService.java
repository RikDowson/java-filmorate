package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired          // Автоматически внедряем зависимость filmStorage
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


//------------------------------ ВЗАИМОДЕЙСТВИЕ С ФИЛЬМОМ --------------------------------------------------------------
    public Map<Integer, Film> findAll() {   // получение всех фильмов
        return filmStorage.getAll();
    }

    public Film add(Film film) {             // добавление фильма
        return filmStorage.add(film);
    }

    public Film update(Film film) {          // обновление фильма
        return filmStorage.update(film);
    }

    public void remove(Integer id) {         // удаление фильма
        filmStorage.remove(id);
    }


//--------------------------------------- ЛАЙКИ ------------------------------------------------------------------------
    public void addLike(Integer filmId, Integer userId) {       // Добавить Лайк
        if (!filmStorage.getAll().containsKey(filmId)) {
            throw new NotFoundException("Фильм " + filmId);
        }
        filmStorage.getAll().get(filmId).getLikes().add(userId);
    }

    public void removeLike(Integer filmId, Integer userId) {    // Удалить Лайк
        if (!filmStorage.getAll().containsKey(filmId)) {
            throw new NotFoundException("Фильм " + filmId);
        }
        if (!getFilm(filmId).getLikes().contains(userId)) {
            throw new NotFoundException("Лайк пользователя " + userId);
        }
        filmStorage.getAll().get(filmId).getLikes().remove(userId);
    }


//---------------------------------- ТОП 10 ФИЛЬМОВ --------------------------------------------------------------------
    public Collection<Film> getTopTenFilms(Integer count) {
        if (count > 0 && count < filmStorage.getAll().size()) {
            return filmStorage.getAll().values().stream()
                    .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                    .limit(count).collect(Collectors.toList());
        }
        return filmStorage.getAll().values().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(filmStorage.getAll().size()).collect(Collectors.toList());
    }

//----------------------------------------------------------------------------------------------------------------------
    public Film getFilm(Integer id) {                     // Получение по id
        if (!filmStorage.getAll().containsKey(id)) {
            throw new NotFoundException("Фильм c id " + id + " не найден");
        }
        return filmStorage.getAll().get(id);
    }
}
