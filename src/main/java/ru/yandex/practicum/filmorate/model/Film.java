package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film extends BaseEntity {

    private String name;
    private String description;
    private LocalDate releaseDate;
    private Set<Long> likes;

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    private Duration duration;
}