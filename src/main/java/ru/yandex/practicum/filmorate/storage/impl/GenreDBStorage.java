package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDBStorage implements GenreStorage {

    @Override
    public Optional<Genre> getById(long id) {
        //TODO
        return Optional.empty();
    }

    @Override
    public List<Genre> getAll() {
        //TODO
        return null;
    }
}
