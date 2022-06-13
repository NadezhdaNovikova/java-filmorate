package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.idFilmGenerator;
import util.idUserGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

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
        if (isNull(film)) {
            throw new ValidationException("Получен пустой запрос");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма отсутствует");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно не превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата релиза не может позже текущей даты");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Некорректная продолжительность фильма");
        }

        film.setId(idFilmGenerator.getId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        if (isNull(film)) {
            throw new ValidationException("Получен пустой запрос");
        }
        if (film.getId() < 1) {
            throw new ValidationException("Обновление невозможно. Некорректный id");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма отсутствует");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно не превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата релиза не может позже текущей даты");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Некорректная продолжительность фильма");
        }
        if (isNull(film.getId())) {
            film.setId(idUserGenerator.getId());
            films.put(film.getId(), film);
        } else if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Указан некорректный id фильма");
        }
        return film;
    }
}
