package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.util.idFilmGenerator;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    Duration duration;
}
