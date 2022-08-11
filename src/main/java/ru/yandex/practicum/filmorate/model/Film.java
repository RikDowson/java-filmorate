package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film implements Comparable<Film> {

    private Integer id;
    @NotEmpty(message = "Поле не может быть пустым")
    private String name;
    @NotBlank
    private String description;
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма не может иметь отрицательное значение")
    private Integer duration;
    private Mpa mpa;
    private Set<Genre> genres;

    private Set<Integer> like = new HashSet<>();

    public void addLike(Integer id) {
        like.add(id);
    }

    public void deleteLike(Integer id) {
        like.remove(id);
    }

    public int compareTo(Film other) {
        return other.getLike().size() - this.getLike().size();
    }
}
