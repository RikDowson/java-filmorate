package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> getAll();            //  Получить всех пользователей

    Optional<User> getFindById(Integer id); // Получить пользователя по id

    User add(User user);            //  Создать пользователя

    User update(User user);         //  Обновить пользователя

    void addFriend(Integer userId, Integer friendId);

    void confirmFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    boolean isUserExist(Integer id);
}
