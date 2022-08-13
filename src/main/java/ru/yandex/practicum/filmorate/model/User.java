package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    @Email
    private String email;
//    @Pattern(regexp = "^\\S*$")
    @Size(max = 30, message = "Имя не может быть больше тридцати символов")
    private String name;
//    @Pattern(regexp = "^\\S*$")
    @Size(max = 30, message = "Имя не может быть больше тридцати символов")
    private String login;
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }


}
