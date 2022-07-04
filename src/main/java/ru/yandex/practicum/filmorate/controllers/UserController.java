package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();
    protected int countId = 1;

    public int generateUserId() {
        return countId++;
    }

    @GetMapping                       // получение списка всех пользователей
    public Collection<User> findAll() throws ValidationException  {
        log.debug("Текущее количество пользователей: {}", users.size());  // логируем факт получения запроса
        return users.values();
    }

    @PostMapping                     // создание пользователя
    public User create(@Valid @RequestBody User user) throws ValidationException {
        validatorUser(user);
        int id = generateUserId();
        user.setId(id);
        log.debug("Добавлен новый пользователь {}{}", id, user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping                     // обновление пользователя
    public User put(@Valid @RequestBody User user) throws ValidationException {
        log.error(String.valueOf((user)));
        validatorUser(user);
        log.debug("Объект POST /user обновлён");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Нет такого пользователя");
        }
        return user;
    }

    public void validatorUser(User userVal) {
        if (userVal.getEmail().isBlank() || !(userVal.getEmail().contains("@"))) {
            log.debug("Электронная почта не может быть пустой и должна содержать символ \"@\"");
            throw new ValidationException("Ошибка! Электронная почта не может быть пустой и должна содержать символ \"@\"");
        }
        if (userVal.getLogin().isBlank()) {
            log.debug("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Ошибка! Логин не может быть пустым и содержать пробелы");
        }
        if (userVal.getName().isEmpty() || userVal.getName() == null) {
            log.debug("Имя для отображения может быть пустым — в таком случае будет использован логин");
            userVal.setName(userVal.getLogin());
        }
        if (userVal.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка! Дата рождения не может быть в будущем.");
        }
    }
}

