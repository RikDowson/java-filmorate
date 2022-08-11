package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired          // Автоматически внедряем зависимость userService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping                       // получение списка всех пользователей
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")              // получение пользователя по id
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PostMapping                     // создание пользователя
    public User addUser(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping                     // обновление пользователя
    public User updateUser(@Valid @RequestBody User user) {
        return userService.update(user);
    }

//------------------------------------------- REST ---------------------------------------------------------------------
    @PutMapping("/{id}/friends/{friendId}")  // обновление пользователя по id
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")  // удаление пользователя по id
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")            // получение друга по id
    public Collection<User> getFriendsOfUser(@PathVariable Integer id) {
        return userService.getFriendsOfUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}

