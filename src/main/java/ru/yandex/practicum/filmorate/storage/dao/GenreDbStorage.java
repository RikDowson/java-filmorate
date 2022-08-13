package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private static JdbcTemplate jdbcTemplate = new JdbcTemplate();

//--------------------------------------- КОНСТАНТЫ --------------------------------------------------------------------
    private static final String SQL_GET_ALL_GENRE = "SELECT * FROM PUBLIC.GENRES ORDER BY ID;";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM PUBLIC.GENRES WHERE ID = ?;";
    private static final String SQL_ADD_GENRE = "INSERT INTO PUBLIC.FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?);";
    private static final String SQL_DELETE_GENRE = "DELETE FROM PUBLIC.FILM_GENRES WHERE FILM_ID = ?;";

//----------------------------------------------------------------------------------------------------------------------


    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        log.info("Получены все жанры");
        return jdbcTemplate.query(SQL_GET_ALL_GENRE, (rs, rowNum) -> makeGenre(rs));
    }

    public static void addGenre(Integer film_id, Integer genre_id) {
        jdbcTemplate.update(SQL_ADD_GENRE, film_id, genre_id);
    }

    public static void deleteGenres(Integer film_id) {
        jdbcTemplate.update(SQL_DELETE_GENRE, film_id);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        List<Genre> genres = jdbcTemplate.query(SQL_FIND_BY_ID, (rs, rowNum) -> makeGenre(rs), id);

        if (genres.isEmpty()) {
            log.info("Жанр с id {} не найден.", id);
            return Optional.empty();
        } else {
            Genre genre = genres.get(0);
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"),
                rs.getString("name")
        );
    }

}
