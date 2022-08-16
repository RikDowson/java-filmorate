package ru.yandex.practicum.filmorate.storage.dao_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(SAME_THREAD)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;

    private final static String NAME = "Аватар";
    private final static String DESCRIPTION = "Конфликт колонистов-десантников и местных жителей";
    private final static Integer DURATION = 180;
    private final static LocalDate RELEASE_DATE = LocalDate.of(1998, 1, 1);
    private final static Mpa MPA = new Mpa(1, null, null);
    private final static Set<Genre> GENRE_SET = Set.of(new Genre(1, null), new Genre(2, null));
    private static Integer userId;

    @Test
    @Order(1)
    void create() {
        Film film = filmDbStorage.add(new Film(null,
                NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, GENRE_SET, null));

        Assertions.assertNotNull(film.getId(), "Не создан фильм!");
        Assertions.assertEquals(1, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, film.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(DESCRIPTION, film.getDescription(), "Некорректное значение поле DESCRIPTION!");
        Assertions.assertEquals(RELEASE_DATE, film.getReleaseDate(), "Некорректное значение поле RELEASE_DATE!");
        Assertions.assertEquals(DURATION, film.getDuration(), "Некорректное значение поле DURATION!");
        Assertions.assertEquals(MPA.getId(), film.getMpa().getId(), "Некорректное значение поле MPA!");
        Assertions.assertEquals(GENRE_SET.size(), film.getGenres().size(), "Некорректное количество жанров!!");
        Assertions.assertEquals(GENRE_SET.stream().map(Genre::getId).collect(Collectors.toSet()),
                film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()),
                "Некорректное id жанров!!");
    }

    @Test
    @Order(2)
    void createSecond() {
        Film film = filmDbStorage.add(new Film(null,
                NAME + "2", DESCRIPTION + "2", RELEASE_DATE.plusMonths(1), DURATION + 2, new Mpa(1, null, null), null, null));

        Assertions.assertNotNull(film.getId(), "Не создан фильм!");
        Assertions.assertEquals(2, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME + "2", film.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(DESCRIPTION + "2", film.getDescription(), "Некорректное значение поле DESCRIPTION!");
        Assertions.assertEquals(RELEASE_DATE.plusMonths(1), film.getReleaseDate(), "Некорректное значение поле RELEASE_DATE!");
        Assertions.assertEquals(DURATION + 2, film.getDuration(), "Некорректное значение поле DURATION!");
        Assertions.assertEquals(MPA.getId(), film.getMpa().getId(), "Некорректное значение поле MPA!");
        Assertions.assertNull(film.getGenres(), "Некорректное значение поле GENRES!");
    }

    @Test
    @Order(3)
    void findById() {
        Optional<Film> filmOptional = filmDbStorage.getById(1);

        Assertions.assertTrue(filmOptional.isPresent(), "Не найден фильм!");
        Film film = filmOptional.get();
        Assertions.assertEquals(1, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, film.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(DESCRIPTION, film.getDescription(), "Некорректное значение поле DESCRIPTION!");
        Assertions.assertEquals(RELEASE_DATE, film.getReleaseDate(), "Некорректное значение поле RELEASE_DATE!");
        Assertions.assertEquals(DURATION, film.getDuration(), "Некорректное значение поле DURATION!");
        Assertions.assertEquals(MPA.getId(), film.getMpa().getId(), "Некорректное значение поле MPA!");
        Assertions.assertEquals(GENRE_SET.size(), film.getGenres().size(), "Некорректное количество жанров!!");
        Assertions.assertEquals(GENRE_SET.stream().map(Genre::getId).collect(Collectors.toSet()),
                film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()),
                "Некорректное id жанров!!");
    }

    @Test
    @Order(4)
    void findAll() {
        List<Film> films = filmDbStorage.getAll();

        Assertions.assertFalse(films.isEmpty(), "Список фильмов пуст!");
        Assertions.assertEquals(2, films.size(), "Некорректное общее количество фильмов!");
        Film film = films.get(0);
        Assertions.assertEquals(1, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, film.getName(), "Некорректное значение поле NAME!");
        Film film2 = films.get(1);
        Assertions.assertEquals(2, film2.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME + "2", film2.getName(), "Некорректное значение поле NAME!");

    }

    @Test
    @Order(5)
    void isFilmExist() {
        boolean isFilmExist = filmDbStorage.isFilmExist(2);
        Assertions.assertTrue(isFilmExist, "Фильм не существует!");
    }

    @Test
    @Order(6)
    void isFilmNotExist() {
        boolean isFilmExist = filmDbStorage.isFilmExist(3);
        Assertions.assertFalse(isFilmExist, "Фильм существует!");
    }

    @Test
    @Order(7)
    void update() {
        String nameNew = "Аватар";
        String descriptionNew = "Конфликт колонистов и местных эителей";
        Integer durationNew = 180;
        LocalDate releaseDateNew = LocalDate.of(1998, 1, 1);
        Mpa mpaNew = new Mpa(1, null, null);
        Set<Genre> genresNew = Set.of(new Genre(2, null));

        Film film = filmDbStorage.update(new Film(2,
                nameNew, descriptionNew, releaseDateNew, durationNew, mpaNew, genresNew, null));

        Assertions.assertEquals(2, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(nameNew, film.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(descriptionNew, film.getDescription(), "Некорректное значение поле DESCRIPTION!");
        Assertions.assertEquals(releaseDateNew, film.getReleaseDate(), "Некорректное значение поле RELEASE_DATE!");
        Assertions.assertEquals(durationNew, film.getDuration(), "Некорректное значение поле DURATION!");
        Assertions.assertEquals(mpaNew.getId(), film.getMpa().getId(), "Некорректное значение поле MPA!");
        Assertions.assertEquals(genresNew.size(), film.getGenres().size(), "Некорректное количество жанров!!");
        Assertions.assertEquals(genresNew.stream().map(Genre::getId).collect(Collectors.toSet()),
                film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()),
                "Некорректное id жанров!!");
    }

    @Test
    @Order(8)
    void addLike() {
        User user = userDbStorage.add(new User(null,
                "teador2022@gmail.com", "Kirill", "Rik_Dowson",
                LocalDate.of(1991, 7, 1), null));
        userId = user.getId();
        likeDbStorage.addLike(1, userId);

        Set<Integer> userIds = filmDbStorage.getLikesById(1);
        Assertions.assertFalse(userIds.isEmpty(), "Список лайков пуст!");
        Assertions.assertEquals(1, userIds.size(), "Некорректное количество лайков!");

    }

    @Test
    @Order(9)
    void deleteLike() {
        likeDbStorage.deleteLike(1, userId);
        Set<Integer> userIds = filmDbStorage.getLikesById(1);
        Assertions.assertTrue(userIds.isEmpty(), "Список лайков не пуст!");
    }
}