package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

//------------------------------ ВЗАИМОДЕЙСТВИЕ С ПОЛЬЗОВАТЕЛЕМ --------------------------------------------------------
    public Map<Integer, User> findAll() {  // получение списка всех пользователей
        return userStorage.getAll();
    }

    public User add(User user) {            // создание пользователя
        return userStorage.add(user);
    }

    public User update(User user) {         // обновление пользователя
        return userStorage.update(user);
    }

    public void remove(Integer id) {        // Удаление пользователя
        userStorage.remove(id);
    }

//------------------------------ ДРУЗЬЯ ПОЛУЧИТЬ/ДОБАВИТЬ/УДАЛИТЬ ------------------------------------------------------
    public User addFriend(Integer id, Integer friendId) {          // Добавить друга
        Map<Integer, User> userMap = userStorage.getAll();

        if (!userStorage.getAll().containsKey(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        if (!userStorage.getAll().containsKey(friendId)) {
            throw new NotFoundException("Друг с id " + friendId + " не найден");
        }
        userMap.get(id).getFriend().add(friendId);   // если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
        userMap.get(friendId).getFriend().add(id);
        return getUser(friendId);
    }

    public void removeFriend(Integer id, Integer removeFromId) {    // Удалить друга
        Map<Integer, User> userMap = userStorage.getAll();

        if (!userStorage.getAll().containsKey(id)) {
            throw new NotFoundException("пользователь " + id);
        }
        if (!userStorage.getAll().containsKey(removeFromId)) {
            throw new NotFoundException("пользователь " + removeFromId);
        }
        userMap.get(id).getFriend().remove(removeFromId);
        userMap.get(removeFromId).getFriend().remove(id);
    }

    public Collection<User> getFriendsOfUser(Integer id) {          // Получить друзей пользователя
        List<User> friends = new ArrayList<>();

        if (!userStorage.getAll().containsKey(id)) {
            throw new NotFoundException("пользователь " + id);
        }
        Set<Integer> userSet = userStorage.getAll().get(id).getFriend();
        for (Integer user : userSet) {
            friends.add(userStorage.getAll().get(user));
        }
        return friends;
    }

    public Collection<User> getMutualFriends(Integer id, Integer id1) {     // Получить общих друзей
        List<User> friendNames = new ArrayList<>();

        if (!userStorage.getAll().containsKey(id)) {
            throw new NotFoundException("пользователь " + id);
        }
        if (!userStorage.getAll().containsKey(id1)) {
            throw new NotFoundException("пользователь " + id1);
        }
        Set<Integer> userSet = userStorage.getAll().get(id).getFriend();
        Set<Integer> userSet1 = userStorage.getAll().get(id1).getFriend();
        for (Integer user : userSet) {
            if (userSet1.contains(user)) {
                friendNames.add(userStorage.getAll().get(user));
            }
        }
        return friendNames;
    }

//----------------------------------------------------------------------------------------------------------------------
    public User getUser(Integer id) {           // Получение пользователя по id
        if (!userStorage.getAll().containsKey(id)) {
            throw new NotFoundException("пользователь " + id);
        }
        return userStorage.getAll().get(id);
    }

}
