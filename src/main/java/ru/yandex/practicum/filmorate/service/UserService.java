package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public static void checkUser(User user) {
        String email = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        String login = user.getLogin();
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(login);
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }

//------------------------------ ВЗАИМОДЕЙСТВИЕ С ПОЛЬЗОВАТЕЛЕМ --------------------------------------------------------
    public List<User> findAll() {  // получение списка всех пользователей
        return userStorage.getAll();
    }

    public User add(User user) {            // создание пользователя
        return userStorage.add(user);
    }

    public User update(User user) {         // обновление пользователя
        return userStorage.update(user);
    }

//------------------------------ ДРУЗЬЯ ПОЛУЧИТЬ/ДОБАВИТЬ/УДАЛИТЬ ------------------------------------------------------
    public User addFriend(Integer id, Integer friendId) {          // Добавить друга
        User user = getUser(id);
        User friend = getUser(friendId);
        if (!user.getFriends().contains(friendId)) {
            userStorage.addFriend(id, friendId);
        }
        if (friend.getFriends().contains(id)) {
            userStorage.confirmFriend(id, friendId);
            userStorage.confirmFriend(friendId, id);
        }
        return user;
    }

    public User removeFriend(Integer id, Integer friendId) {    // Удалить друга
        User user = getUser(id);
        User friend = getUser(friendId);
        if (user.getFriends().contains(friendId)) {
            userStorage.deleteFriend(id, friendId);
            userStorage.deleteFriend(friendId, id);
        }
        return user;
    }

    public List<User> getFriendsOfUser(Integer id) {          // Получить друзей пользователя
        User user = getUser(id);
        return user.getFriends()
                .stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Integer id, Integer id1) {     // Получить общих друзей
        User user1 = getUser(id);
        User user2 = getUser(id1);
        return user1.getFriends()
                .stream()
                .filter(user2.getFriends()::contains)
                .map(this::getUser)
                .collect(Collectors.toList());
    }

//----------------------------------------------------------------------------------------------------------------------
    public User getUser(Integer id) {           // Получение пользователя по id
        return userStorage.getFindById(id).orElseThrow(
                () -> new NotFoundException("Пользователя с id = " + id + " не существует!")
        );
    }

    public void checkUserForExist(Integer id) {
        if (!userStorage.isUserExist(id)) {
            throw new NotFoundException("Пользователя с id = " + id + " не существует!");
        }
    }

}
