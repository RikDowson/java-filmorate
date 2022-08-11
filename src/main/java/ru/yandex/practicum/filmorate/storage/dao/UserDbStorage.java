package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.util.*;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

//--------------------------------------- КОНСТАНТЫ --------------------------------------------------------------------
    private static final String SQL_GET_ALL_USERS = "SELECT * FROM PUBLIC.USERS ORDER BY ID;";
    private static final String SQL_GET_BY_ID_USERS = "SELECT * FROM PUBLIC.USERS WHERE ID = ?;";
    private static final String SQL_CREATE_USER = "INSERT INTO PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)" +
            " VALUES (?, ?, ?, ?);";
    private static final String SQL_UPDATE_USER =  "UPDATE PUBLIC.USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, " +
            "BIRTHDAY = ? " +
            "WHERE id = ?;";
    private static final String SQL_ADD_FREND = "INSERT INTO PUBLIC.FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?);";
    private static final String SQL_CONFIRM = "UPDATE PUBLIC.FRIENDS SET IS_CONFIRMED = ?" +
            "WHERE USER_ID = ? AND FRIEND_ID = ?;";
    private static final String SQL_DELETE_FREND = "DELETE FROM PUBLIC.FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?;";
    private static final String SQL_DELETE_FREND_BY_ID = "SELECT FRIEND_ID FROM PUBLIC.FRIENDS WHERE USER_ID = ?;";
    private static final String SQL_GET_STATUS_FREND = "SELECT IS_CONFIRMED FROM PUBLIC.FRIENDS WHERE USER_ID = ?" +
            " AND FRIEND_ID = ?";
//----------------------------------------------------------------------------------------------------------------------

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query(SQL_GET_ALL_USERS, (rs, rowNum) -> makeUser(rs));
        users.forEach(u -> u.setFriends(getFriendsById(u.getId())));
        return users;
    }

    @Override
    public Optional<User> getFindById(Integer id) {
        List<User> users = jdbcTemplate.query(SQL_GET_BY_ID_USERS, (rs, rowNum) -> makeUser(rs), id);

        if (users.isEmpty()) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        } else {
            User user = users.get(0);
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            user.setFriends(getFriendsById(user.getId()));
            return Optional.of(user);
        }
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SQL_CREATE_USER, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(SQL_UPDATE_USER,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        user.setFriends(getFriendsById(user.getId()));
        return user;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update(SQL_ADD_FREND, userId, friendId);
        getFindById(userId).ifPresent(u -> u.setFriends(getFriendsById(userId)));
    }

    @Override
    public void confirmFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update(SQL_CONFIRM, true, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update(SQL_DELETE_FREND, userId, friendId);
        getFindById(userId).ifPresent(u -> u.setFriends(getFriendsById(userId)));
    }

    public Set<Integer> getFriendsById(Integer id) {
        List<Integer> friends = jdbcTemplate.query(SQL_DELETE_FREND_BY_ID, (rs, rowNum) -> getFriendId(rs), id);
        return Set.copyOf(friends);
    }

    public Boolean getStatusFriends(Integer userId, Integer friendId) {
        Boolean isConfirmed = null;
        List<Boolean> isConfirmedList = jdbcTemplate.query(SQL_GET_STATUS_FREND,
                (rs, rowNum) -> getStatusFriends(rs), userId, friendId);
        if (!isConfirmedList.isEmpty()) {
            isConfirmed = isConfirmedList.get(0);
        }
        return isConfirmed;
    }

    @Override
    public boolean isUserExist(Integer id) {
        return getFindById(id).isPresent();
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                null
        );
    }

    private Integer getFriendId(ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }

    private Boolean getStatusFriends(ResultSet rs) throws SQLException {
        return rs.getBoolean("is_confirmed");
    }

}