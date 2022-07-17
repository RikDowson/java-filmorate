package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    protected Integer countId = 1;

    public int generateFilmId() {
        return countId++;
    }


    // получение всех фильмов
    public Map<Integer, Film> getAll() throws ValidationException {
        log.info("Общее количество фильмов: {}", films.size());  // логируем факт получения запроса
        return films;
    }

    // добавление фильма
    public Film add(@Valid @RequestBody Film film) throws ValidationException {
        int id = generateFilmId();
        film.setId(id);
        validation(film);
        log.info("Добавлен новый фильм: {}{}", id, film.getName());
        films.put(film.getId(), film);
        return film;
    }

    // обновление фильма
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        validation(film);
        log.info("Объект POST /film обновлён");
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Фильма с id " + film.getId() + " нет.");
        }
        return film;
    }

    // удаление фильма
    public void remove(Integer id) {
        films.remove(id);
        log.info("Удален фильм {}.", id);
    }

    public void validation(Film filmVal) {
        if (filmVal.getName() == null || filmVal.getName().isBlank()) {
            log.info("Название фильма не может быть пустым");
            throw new ValidationException("Ошибка ввода! Название фильма не может быть пустым.");
        }
        if (filmVal.getDescription().length() > 200) {
            log.info("Максимальная длина описания — 200 символов");
            throw new ValidationException("Ошибка ввода! Максимальная длина описания — 200 символов");
        }
        if (filmVal.getReleaseDate().isBefore(LocalDate.of(1895, 12, 27))) {
            log.info("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Ошибка ввода! Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (filmVal.getDuration() <= 0) {
            log.info("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Ошибка ввода! продолжительность фильма должна быть положительной.");
        }
    }
}
