package ru.yandex.practicum.filmorate.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Film extends BaseEntity {

    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Long> likes = new HashSet<>();
    private Integer rate = 0;

    public Film(Long id) {
        super(id);
    }

    public Film() {
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Set<Long> getLikes() {
        return likes;
    }

    public void setLikes(Set<Long> likes) {
        this.likes = likes;
    }

    public Integer getRate() {
        return rate;
    }

    public void addLike(Long id) {
        likes.add(id);
        rate = likes.size();
    }

    public void removeLike(Long id) {
        likes.remove(id);
        rate = likes.size();
    }
}