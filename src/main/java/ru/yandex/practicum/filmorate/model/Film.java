package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film extends BaseEntity{

    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> genres = new HashSet<>();
    private int mpa;

}