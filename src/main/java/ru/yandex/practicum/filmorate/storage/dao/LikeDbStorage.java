package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component("likeDbStorage")
public class LikeDbStorage implements LikeStorage {

    private JdbcTemplate jdbcTemplate;
    private FilmStorage filmStorage;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate,
                                @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
    }

//--------------------------------------- КОНСТАНТЫ --------------------------------------------------------------------
    private static final String SQL_ADD_LIKE = "INSERT INTO PUBLIC.LIKES (FILM_ID, USER_ID) VALUES (?, ?);";
    private static final String SQL_DELETE_LIKE = "DELETE FROM PUBLIC.LIKES WHERE FILM_ID = ? AND USER_ID = ?;";
    private static final String SQL_DELETE_LIKE_ID = "SELECT USER_ID FROM PUBLIC.LIKES WHERE FILM_ID = ?;";

//--------------------------------------- ЛАЙКИ ------------------------------------------------------------------------
    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_ADD_LIKE, filmId, userId);
        filmStorage.getById(filmId).ifPresent(f -> f.setLike(getLikesById(filmId)));
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_DELETE_LIKE, filmId, userId);
        filmStorage.getById(filmId).ifPresent(f -> f.setLike(getLikesById(filmId)));
    }

    public Set<Integer> getLikesById(Integer id) {
        List<Integer> likes = jdbcTemplate.query(SQL_DELETE_LIKE_ID, (rs, rowNum) -> getLikeUserId(rs), id);
        return Set.copyOf(likes);
    }

//----------------------------------------------------------------------------------------------------------------------

    private Integer getLikeUserId(ResultSet rs) throws SQLException {
        return rs.getInt("user_id");
    }
}
