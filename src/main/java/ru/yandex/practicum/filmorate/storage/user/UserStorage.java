package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getAll();   //  Получить всех пользователей

    User add(User user);            //  Создать пользователя

    User update(User user);         //  Обновить пользователя

    void remove(Integer id);        //  Удалить пользователя
}
