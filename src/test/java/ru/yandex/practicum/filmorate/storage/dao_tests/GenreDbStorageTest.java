package ru.yandex.practicum.filmorate.storage.dao_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    void findAll() {
        List<Genre> genres = genreDbStorage.getAll();

        Assertions.assertFalse(genres.isEmpty(), "Справочник жанров пуст!");
        Assertions.assertEquals(6, genres.size(), "Некорректное общее количество строк справочника жанров!");
        Assertions.assertEquals(6, genres.get(5).getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals("Боевик", genres.get(5).getName(), "Некорректное значение поле NAME!");
    }

    @Test
    void findById() {
        Optional<Genre> genre = genreDbStorage.findById(1);

        Assertions.assertTrue(genre.isPresent(), "Не найдена строка в справочнике жанров!");
        Assertions.assertEquals(1, genre.get().getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals("Комедия", genre.get().getName(), "Некорректное значение поле NAME!");
    }
}
