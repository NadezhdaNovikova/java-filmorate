package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class Friendship {
    @NonNull
    private final Optional<User> user;
    @NonNull
    private final Optional<User> friend;

}