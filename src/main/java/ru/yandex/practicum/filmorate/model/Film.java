package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film extends BaseEntity {

    private String name;
    private String description;
    private LocalDate releaseDate;

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    private Duration duration;
}