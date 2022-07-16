package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
//    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();
    protected int countId = 1;

    public int generateUserId() {
        return countId++;
    }


    // Получение списка всех пользователей
    public Map<Integer, User> getAllUsers() throws ValidationException {
        log.debug("Текущее количество пользователей: {}", users.size());  // логируем факт получения запроса
        return users;
    }

    // Создание пользователя
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        validation(user);
        int id = generateUserId();
        user.setId(id);
        log.debug("Добавлен новый пользователь {}{}", id, user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    // Обновление пользователя
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.error(String.valueOf((user)));
        validation(user);
        log.debug("Объект POST /user обновлён");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Нет такого пользователя");
        }
        return user;
    }

    // Удаление пользователя
    public void removeUser(Integer id) {
        users.remove(id);
        log.debug("Удален пользователь {}", id);
    }


    public void validation(User userVal) {
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
