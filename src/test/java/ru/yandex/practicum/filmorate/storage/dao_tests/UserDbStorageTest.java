package ru.yandex.practicum.filmorate.storage.dao_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(SAME_THREAD)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private final static String NAME = "Kirill";
    private final static String LOGIN = "Kirill";
    private final static String EMAIL = "teador2022@gmail.com";
    private final static LocalDate BIRTHDAY = LocalDate.of(1991, 7, 1);

    @Test
    @Order(1)
    void create() {
        User user = userDbStorage.add(new User(null, EMAIL, LOGIN, NAME, BIRTHDAY, null));

        Assertions.assertNotNull(user.getId(), "Не создан пользователь!");
        Assertions.assertEquals(1, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(LOGIN, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals(EMAIL, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(BIRTHDAY, user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }

    @Test
    @Order(2)
    void createSecond() {
        User user = userDbStorage.add(new User(null,
                "Second" + EMAIL, "Second" + LOGIN, "Second" + NAME, BIRTHDAY.plusMonths(1), null));

        Assertions.assertNotNull(user.getId(), "Не создан пользователь!");
        Assertions.assertEquals(2, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals("Second" + NAME, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals("Second" + LOGIN, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals("Second" + EMAIL, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(BIRTHDAY.plusMonths(1), user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }

    @Test
    @Order(3)
    void findById() {
        Optional<User> userOptional = userDbStorage.getFindById(1);

        Assertions.assertTrue(userOptional.isPresent(), "Не найден пользователь!");
        User user = userOptional.get();
        Assertions.assertEquals(1, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(LOGIN, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals(EMAIL, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(BIRTHDAY, user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }

    @Test
    @Order(4)
    void findAll() {
        List<User> users = userDbStorage.getAll();

        Assertions.assertFalse(users.isEmpty(), "Список пользователей пуст!");
        Assertions.assertEquals(2, users.size(), "Некорректное общее количество пользователей!");
        User user = users.get(0);
        Assertions.assertEquals(1, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(LOGIN, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals(EMAIL, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(BIRTHDAY, user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }


    @Test
    @Order(5)
    void isUserExist() {
        boolean isUserExist = userDbStorage.isUserExist(2);
        Assertions.assertTrue(isUserExist, "Пользователь не существует!");
    }

    @Test
    @Order(6)
    void isUserNotExist() {
        boolean isUserExist = userDbStorage.isUserExist(3);
        Assertions.assertFalse(isUserExist, "Пользователь существует!");
    }

    @Test
    @Order(7)
    void update() {
        String nameNew = "Vitaly";
        String loginNew = "Vitaly";
        String emailNew = "vit@mail.ru";
        LocalDate birthdayNew = LocalDate.of(1991, 2, 12);
        User user = userDbStorage.update(new User(2, emailNew, nameNew, loginNew, birthdayNew, null));

        Assertions.assertEquals(2, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(emailNew, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(nameNew, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(loginNew, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals(birthdayNew, user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }

    @Test
    @Order(8)
    void addFriend() {
        userDbStorage.addFriend(1, 2);

        Set<Integer> friendIds = userDbStorage.getFriendsById(1);
        Assertions.assertFalse(friendIds.isEmpty(), "Список друзей пуст!");
        Assertions.assertEquals(1, friendIds.size(), "Некорректное общее количество друзей!");

        Boolean isConfirmed = userDbStorage.getStatusFriends(1, 2);
        Assertions.assertFalse(isConfirmed, "Дружба подтверждена!");

        Set<Integer> friendId2s = userDbStorage.getFriendsById(2);
        Assertions.assertTrue(friendId2s.isEmpty(), "Список друзей не пуст!");
    }

    @Test
    @Order(9)
    void confirmFriend() {
        userDbStorage.confirmFriend(1, 2);
        Boolean isConfirmed = userDbStorage.getStatusFriends(1, 2);
        Assertions.assertTrue(isConfirmed, "Дружба не подтверждена!");
    }

    @Test
    @Order(10)
    void deleteFriend() {
        userDbStorage.deleteFriend(1, 2);
        Set<Integer> friendIds = userDbStorage.getFriendsById(1);
        Assertions.assertTrue(friendIds.isEmpty(), "Список друзей не пуст!");
    }
}