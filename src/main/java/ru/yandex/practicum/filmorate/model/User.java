package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.util.idUserGenerator;

import java.time.LocalDate;

@Data
public class User {
    int id = idUserGenerator.getId();
    String email;
    String login;
    String name;
    LocalDate birthday;
}
