package ru.yandex.practicum.filmorate.service_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

class UserServiceTest {
    private final User user = new User();
    private static final String MESSAGE_USER_EMAIL = "Электронная почта не может быть пустой и должна содержать символ @!";
    private static final String MESSAGE_USER_LOGIN = "Логин не может быть пустым и содержать пробелы!";
    private static final String MESSAGE_USER_BIRTHDAY = "Дата рождения не может быть в будущем!";

    @BeforeEach
    void setUp() {
        user.setName("Kirill");
        user.setLogin("Rik_Dowson");
        user.setEmail("rik_d@mail.ru");
        user.setBirthday(LocalDate.of(1991, 7, 1));
    }

    @Test
    void createEmptyUser() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> UserService.checkUser(new User()));
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    void createUser() throws ValidationException {
        UserService.checkUser(user);
    }

    @Test
    void createUserNullEmail() {
        user.setEmail(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> UserService.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_EMAIL, exception.getMessage());
    }

    @Test
    void createUserEmptyEmail() {
        user.setEmail("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> UserService.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_EMAIL, exception.getMessage());
    }

    @Test
    void createUserWithoutAtEmail() {
        user.setEmail("Kirill");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> UserService.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_EMAIL, exception.getMessage());
    }

    @Test
    void createUserNullLogin() {
        user.setLogin(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> UserService.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_LOGIN, exception.getMessage());
    }

    @Test
    void createUserEmptyLogin() {
        user.setLogin("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> UserService.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_LOGIN, exception.getMessage());
    }

    @Test
    void createUserWithSpaceLogin() {
        user.setLogin("Rik Dowson");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> UserService.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_LOGIN, exception.getMessage());
    }

    @Test
    void createUserNullBirthDay() throws ValidationException {
        user.setBirthday(null);
        System.out.println(user);
        UserService.checkUser(user);
    }

    @Test
    void createUserBirthDayFuture() {
        user.setBirthday(LocalDate.now().plusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> UserService.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_BIRTHDAY, exception.getMessage());
    }

    @Test
    void createUserNullName() throws ValidationException {
        user.setName(null);
        UserService.checkUser(user);
    }

}