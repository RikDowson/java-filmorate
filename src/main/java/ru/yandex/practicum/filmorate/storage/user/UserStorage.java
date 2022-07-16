package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getAllUsers();   //  Получить всех пользователей

    User addUser(User user);            //  Создать пользователя

    User updateUser(User user);         //  Обновить пользователя

    void removeUser(Integer id);        //  Удалить пользователя
}
