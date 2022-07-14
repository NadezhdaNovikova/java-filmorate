package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.GenreDBStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreDBStorage genreDBStorage;

    @Autowired
    public GenreController(GenreDBStorage genreDBStorage) {
        this.genreDBStorage = genreDBStorage;
    }

    @GetMapping("/genres")
    public List<Genre> getAll() {
        return genreDBStorage.getAll();
    }

    @GetMapping("/genres/{id}")
    public Optional<Genre> getById(@PathVariable("id") final Long id) throws EntityNotFoundException {
        return Optional.ofNullable(genreDBStorage.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Жанр с id = %s не найден", id))));
    }

}