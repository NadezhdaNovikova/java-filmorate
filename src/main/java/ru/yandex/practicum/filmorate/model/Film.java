package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.util.idFilmGenerator;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    int id = idFilmGenerator.getId();
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
}
