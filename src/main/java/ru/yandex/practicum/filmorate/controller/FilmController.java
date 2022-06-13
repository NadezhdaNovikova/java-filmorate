package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @RequestMapping("/films")

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
        System.out.println(film.getId());
        log.info(String.valueOf(film.getDuration()));
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateUser(@RequestBody Film film) {
        if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
        }
        return film;
    }
}
