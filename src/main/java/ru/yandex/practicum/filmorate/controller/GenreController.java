package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController

public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @RequestMapping("/genres")

    @GetMapping("/genres")
    public List<Genre> getAll() {
        return genreService.getAll();
    }

    @GetMapping("/genres/{id}")
    public Optional<Genre> getById(@PathVariable("id") final Long id) throws EntityNotFoundException {
        return Optional.ofNullable(genreService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Жанр с id = %s не найден", id))));
    }

}