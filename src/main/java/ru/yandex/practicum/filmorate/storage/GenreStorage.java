package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    Optional<Genre> getById(long id);
    List<Genre> getAll();
    Set<Genre> getFilmGenres(long id);
}