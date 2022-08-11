package ru.yandex.practicum.filmorate.service_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

class FilmServiceTest {
    private final Film film = new Film();
    private static final String MESSAGE_FILM_NAME = "Название фильма не может быть пустым!";
    private static final String MESSAGE_FILM_DESC = "Максимальная фильма длина описания — 200 символов!";
    private static final String MESSAGE_FILM_RELEASE_DATE = "Дата релиза — не раньше 28 декабря 1895 года!";
    private static final String MESSAGE_FILM_DURATION = "Продолжительность фильма должна быть положительной!";

    @BeforeEach
    void setUp() {
        film.setName("Titanic");
        film.setDescription("About ship Titanic");
        film.setDuration(180);
        film.setReleaseDate(LocalDate.of(1998, 1, 1));
    }

    @Test
    void createFilm() throws ValidationException {
        FilmService.checkFilm(film);
    }

    @Test
    void createFilmNullName() {
        film.setName(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_NAME, exception.getMessage());
    }

    @Test
    void createFilmEmptyName() {
        film.setName("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_NAME, exception.getMessage());
    }

    @Test
    void createFilmDescNull() throws ValidationException {
        film.setDescription(null);
        FilmService.checkFilm(film);
    }

    @Test
    void createFilmDesc200() throws ValidationException {
        String newDesc = "Джейк Салли — бывший морской пехотинец," +
                " прикованный к инвалидному креслу. Несмотря на немощное тело," +
                " Джейк в душе по-прежнему остается воином." +
                " Он получает задание совершить путешествие в несколько св";
        film.setDescription(newDesc);
        FilmService.checkFilm(film);
    }

    @Test
    void createFilmDescMore200() {
        film.setDescription("Джейк Салли — бывший морской пехотинец, прикованный к инвалидному креслу." +
                " Несмотря на немощное тело, Джейк в душе по-прежнему остается воином." +
                " Он получает задание совершить путешествие в несколько световых лет к базе землян на планете Пандора," +
                " где корпорации добывают редкий минерал, " +
                "имеющий огромное значение для выхода Земли из энергетического кризиса.");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DESC, exception.getMessage());
    }

    @Test
    void createFilmReleaseDateNull() throws ValidationException {
        film.setReleaseDate(null);
        FilmService.checkFilm(film);
    }

    @Test
    void createFilmReleaseDate28121895() throws ValidationException {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        FilmService.checkFilm(film);
    }

    @Test
    void createFilmReleaseDateLess28121895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28).minusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_RELEASE_DATE, exception.getMessage());
    }

    @Test
    void createFilmDurationNull() throws ValidationException {
        film.setDuration(null);
        FilmService.checkFilm(film);
    }

    @Test
    void createFilmDurationEqual0() {
        film.setDuration(0);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DURATION, exception.getMessage());
    }

    @Test
    void createFilmDurationLess0() {
        film.setDuration(-20);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DURATION, exception.getMessage());
    }
}