package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    private final LikeStorage likeStorage;

    @Autowired          // Автоматически внедряем зависимость filmStorage, userService
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService,
                       @Qualifier("likeDbStorage") LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likeStorage = likeStorage;
    }

//------------------------------ ВЗАИМОДЕЙСТВИЕ С ФИЛЬМОМ --------------------------------------------------------------
    public List<Film> findAll() {   // получение всех фильмов
        return filmStorage.getAll();
    }

    public Film getFilm(Integer id) {                     // Получение по id
        return filmStorage.getById(id).orElseThrow(
                () -> new NotFoundException("Фильм c id " + id + " не найден")
        );
    }

    public Film add(Film film) {             // добавление фильма
        checkFilm(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {          // обновление фильма
        checkFilm(film);
        checkFilmForExist(film.getId());
        return filmStorage.update(film);
    }

//--------------------------------------- ЛАЙКИ ------------------------------------------------------------------------
    public Film addLike(Integer id, Integer userId) {         // Добавить Лайк
        Film film = getFilm(id);
        userService.checkUserForExist(userId);
        if (!film.getLike().contains(userId)) {
            likeStorage.addLike(id, userId);
        }
        return film;
    }

    public Film removeLike(Integer id, Integer userId) {      // Удалить Лайк
        Film film = getFilm(id);
        userService.checkUserForExist(userId);
        if (film.getLike().contains(userId)) {
            likeStorage.deleteLike(id, userId);
        }
        return film;
    }


//---------------------------------- ТОП 10 ФИЛЬМОВ --------------------------------------------------------------------
    public List<Film> getTopTenFilms(Integer count) {
        return filmStorage.getAll()
                .stream()
                .sorted()
                .limit(count)
                .collect(Collectors.toList());
    }

//--------------------------------- ПРОВЕРКА ФИЛЬМА --------------------------------------------------------------------
    public static void checkFilm(Film film) {
        String name = film.getName();
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым!");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная фильма длина описания — 200 символов!");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }
        if (film.getDuration() != null && film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
    }

    public void checkFilmForExist(Integer id) {
        if (!filmStorage.isFilmExist(id)) {
            throw new NotFoundException("Фильма с id " + id + " не существует!");
        }
    }
}