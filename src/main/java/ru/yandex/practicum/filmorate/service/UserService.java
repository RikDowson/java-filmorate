package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

//------------------------------ ВЗАИМОДЕЙСТВИЕ С ПОЛЬЗОВАТЕЛЕМ --------------------------------------------------------
    public Map<Integer, User> findAllUsers() {  // получение списка всех пользователей
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {            // создание пользователя
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {         // обновление пользователя
        return userStorage.updateUser(user);
    }

    public void removeUser(Integer id) {        // Удаление пользователя
        userStorage.removeUser(id);
    }

//------------------------------ ДРУЗЬЯ ПОЛУЧИТЬ/ДОБАВИТЬ/УДАЛИТЬ ------------------------------------------------------
    public User addFriend(Integer id, Integer friendId) {          // Добавить друга
        Map<Integer, User> userMap = userStorage.getAllUsers();

        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        if (!userStorage.getAllUsers().containsKey(friendId)) {
            throw new NotFoundException("Друг с id " + friendId + " не найден");
        }
        userMap.get(id).getFriend().add(friendId);   // если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
        userMap.get(friendId).getFriend().add(id);
        return getUser(friendId);
    }

    public void removeFriend(Integer id, Integer removeFromId) {    // Удалить друга
        Map<Integer, User> userMap = userStorage.getAllUsers();

        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new NotFoundException("пользователь " + id);
        }
        if (!userStorage.getAllUsers().containsKey(removeFromId)) {
            throw new NotFoundException("пользователь " + removeFromId);
        }
        userMap.get(id).getFriend().remove(removeFromId);
        userMap.get(removeFromId).getFriend().remove(id);
    }

    public Collection<User> getFriendsOfUser(Integer id) {          // Получить друзей пользователя
        List<User> friends = new ArrayList<>();

        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new NotFoundException("пользователь " + id);
        }
        Set<Integer> userSet = userStorage.getAllUsers().get(id).getFriend();
        for (Integer user : userSet) {
            friends.add(userStorage.getAllUsers().get(user));
        }
        return friends;
    }

    public Collection<User> getMutualFriends(Integer id, Integer id1) {     // Получить общих друзей
        List<User> friendNames = new ArrayList<>();

        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new NotFoundException("пользователь " + id);
        }
        if (!userStorage.getAllUsers().containsKey(id1)) {
            throw new NotFoundException("пользователь " + id1);
        }
        Set<Integer> userSet = userStorage.getAllUsers().get(id).getFriend();
        Set<Integer> userSet1 = userStorage.getAllUsers().get(id1).getFriend();
        for (Integer user : userSet) {
            if (userSet1.contains(user)) {
                friendNames.add(userStorage.getAllUsers().get(user));
            }
        }
        return friendNames;
    }

//----------------------------------------------------------------------------------------------------------------------
    public User getUser(Integer id) {           // Получение пользователя по id
        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new NotFoundException("пользователь " + id);
        }
        return userStorage.getAllUsers().get(id);
    }

}
