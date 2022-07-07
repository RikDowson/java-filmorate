package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class User {

    private int id;
    @Email
    private String email;
    @Pattern(regexp = "^\\S*$")
    @Size(max = 30, message = "Имя не может быть больше тридцати символов")
    private String name;
    @Pattern(regexp = "^\\S*$")
    @Size(max = 30, message = "Имя не может быть больше тридцати символов")
    private String login;
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
