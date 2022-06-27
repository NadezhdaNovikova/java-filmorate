package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User extends BaseEntity {

    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> likes;

}