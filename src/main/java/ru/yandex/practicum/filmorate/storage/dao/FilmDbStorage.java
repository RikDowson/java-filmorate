package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

//--------------------------------------- КОНСТАНТЫ --------------------------------------------------------------------
    private static final String SQL_FIND_ALL = "SELECT * FROM PUBLIC.FILMS ORDER BY ID;";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM PUBLIC.FILMS WHERE ID = ?;";
    private static final String SQL_CREATE_FILM = "INSERT INTO PUBLIC.FILMS (NAME," +
            " DESCRIPTION," +
            " RELEASE_DATE," +
            " DURATION," +
            " MPA_ID)\n" +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE_FILM = "UPDATE PUBLIC.FILMS " +
            "SET NAME = ?, " +
            "DESCRIPTION = ?, " +
            "RELEASE_DATE = ?, " +
            "DURATION = ?, " +
            "MPA_ID = ? " +
            "WHERE id = ?;";
    private static final String SQL_ADD_LIKE = "INSERT INTO PUBLIC.LIKES (FILM_ID, USER_ID) VALUES (?, ?);";
    private static final String SQL_DELETE_LIKE = "DELETE FROM PUBLIC.LIKES WHERE FILM_ID = ? AND USER_ID = ?;";
    private static final String SQL_DELETE_LIKE_ID = "SELECT USER_ID FROM PUBLIC.LIKES WHERE FILM_ID = ?;";

    private static final String SQL_GET_GENRE_BY_FILM_ID = "SELECT GENRE_ID" +
            " FROM PUBLIC.FILM_GENRES WHERE FILM_ID = ? ORDER BY GENRE_ID;";

//----------------------------------------------------------------------------------------------------------------------

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbcTemplate.query(SQL_FIND_ALL, (rs, rowNum) -> makeFilm(rs));
        films.forEach(f -> {
            f.setLike(getLikesById(f.getId()));
            f.setGenres(getGenresByFilmId(f.getId()));
        });
        log.info("Получены все фильмы");
        return films;
    }

    @Override
    public Optional<Film> getById(Integer id) {
        List<Film> films = jdbcTemplate.query(SQL_FIND_BY_ID, (rs, rowNum) -> makeFilm(rs), id);
        if (films.isEmpty()) {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        } else {
            Film film = films.get(0);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            film.setLike(getLikesById(film.getId()));
            film.setGenres(getGenresByFilmId(film.getId()));
            log.info("Получен фильм с идентификатором: {}", film.getId());
            return Optional.of(film);
        }
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE_FILM, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setInt(4, film.getDuration());
            ps.setObject(5, film.getMpa().getId());
            return ps;
            },
                keyHolder);
        film.setId(keyHolder.getKey().intValue());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            film.getGenres().forEach(g -> GenreDbStorage.addGenre(film.getId(), g.getId()));
        }
        log.info("Добавлен фильм с идентификатором: {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(SQL_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        film.setMpa(mpaDbStorage.findById(film.getMpa().getId()).orElse(null));
        if (film.getGenres() != null) {
            GenreDbStorage.deleteGenres(film.getId());
            if (!film.getGenres().isEmpty()) {
                film.getGenres().forEach(g -> GenreDbStorage.addGenre(film.getId(), g.getId()));
                film.setGenres(getGenresByFilmId(film.getId()));
            }
        }
        film.setLike(getLikesById(film.getId()));
        log.info("Обновлен фильм с идентификатором: {}", film.getId());
        return film;
    }

//--------------------------------------- ЛАЙКИ ------------------------------------------------------------------------
    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_ADD_LIKE, filmId, userId);
        getById(filmId).ifPresent(f -> f.setLike(getLikesById(filmId)));
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_DELETE_LIKE, filmId, userId);
        getById(filmId).ifPresent(f -> f.setLike(getLikesById(filmId)));
    }

    public Set<Integer> getLikesById(Integer id) {
        List<Integer> likes = jdbcTemplate.query(SQL_DELETE_LIKE_ID, (rs, rowNum) -> getLikeUserId(rs), id);
        return Set.copyOf(likes);
    }
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isFilmExist(Integer id) {
        return getById(id).isPresent();
    }

    private Set<Genre> getGenresByFilmId(Integer film_id) {
        List<Integer> genreIds = jdbcTemplate.query(SQL_GET_GENRE_BY_FILM_ID, (rs, rowNum) -> getGenreId(rs), film_id);
        if (genreIds.isEmpty()) {
            return null;
        } else {
            return genreIds.stream().map(id -> genreDbStorage.findById(id)
                    .orElse(new Genre())).collect(Collectors.toSet());
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer mpa_id = rs.getInt("mpa_id");
        Mpa mpa = mpaDbStorage.findById(mpa_id).orElse(new Mpa());
        return new Film(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                mpa,
                null,
                null
        );
    }
    // получить лайк идентификатор пользователя
    private Integer getLikeUserId(ResultSet rs) throws SQLException {
        return rs.getInt("user_id");
    }
    // получить идентификатор жанра
    private Integer getGenreId(ResultSet rs) throws SQLException {
        return rs.getInt("genre_id");
    }
}
