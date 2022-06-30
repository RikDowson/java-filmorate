package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final List<User> users = new ArrayList<>();


    @GetMapping                       // получение списка всех пользователей
    public List<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());  // логируем факт получения запроса
        return users;
    }

    @PostMapping                     // создание пользователя
    public User create(@RequestBody User user) {
        if (user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            throw new ValidationException("Ошибка! Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException("Ошибка! Логин не может быть пустым и содержать пробелы");
        } else if (user.getName().isEmpty()) {
            System.out.println(user.getLogin());    // ???
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка! Дата рождения не может быть в будущем.");
        }
        log.info("Объект POST /user сохранен");
        users.add(user);
        return user;
    }

    @PutMapping                     // обновление пользователя
    public User put(@RequestBody User user) {
        if (user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            throw new ValidationException("Ошибка! Электронная почта не может быть пустой и должна содержать символ \"@\"");
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException("Ошибка! Логин не может быть пустым и содержать пробелы");
        } else if (user.getName().isEmpty()) { //имя для отображения может быть пустым — в таком случае будет использован логин
            System.out.println(user.getLogin());    // ???
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка! Дата рождения не может быть в будущем.");
        }
        log.info("Объект POST /user обновлён");
        users.add(user);
        return user;
    }

}
