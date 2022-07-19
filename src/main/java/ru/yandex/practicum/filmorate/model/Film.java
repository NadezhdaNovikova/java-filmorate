package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film extends BaseEntity{

    @NotNull(message = "Отсутствует название фильма")
    @Size(min = 1, message = "Название фильма не может быть меньше {min} символа/ов")
    private String name;

    @NotNull(message = "Отсутствует описание фильма")
    @Size(min = 1, max = 200, message = "Описание фильма должно содержать от {min} до {max} символа/ов")
    private String description;

    @NotNull(message = "Отсутствует дата релиза фильма")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Некорректная продолжительность фильма. Должна быть больше 0." )
    private int duration;

    @NotNull(message = "Отсутствует рейтинг фильма.")
    private Mpa mpa;

    private Set<Genre> genres;

    private int rate;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}