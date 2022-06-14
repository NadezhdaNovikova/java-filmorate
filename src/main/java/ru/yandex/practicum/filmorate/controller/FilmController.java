package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@RestController
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @Autowired
    private IdGenerator idFilmGenerator;

    @RequestMapping("/films")

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
        filmValidate(film);
        film.setId(idFilmGenerator.getId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        filmValidate(film);
        if (film.getId() < 1 | !films.containsKey(film.getId())) {
            log.info("id: " + film.getId());
            throw new ValidationException("Обновление невозможно. Некорректный id");
        }
        films.put(film.getId(), film);
        return film;
    }

    private void filmValidate(Film film) {
        if (isNull(film)) {
            log.info("Получен запрос: " + film);
            throw new ValidationException("Получен пустой запрос");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Название фильма: " + film.getName());
            throw new ValidationException("Название фильма отсутствует");
        }
        if (film.getDescription().length() > 200) {
            log.info("Длина описания фильма: " + film.getDescription().length() + " символов.");
            throw new ValidationException("Описание фильма должно не превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Дата релиза: " + film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getReleaseDate().isAfter(LocalDate.now())) {
            log.info("Дата релиза: " + film.getReleaseDate() + " Текущая дата " + LocalDate.now());
            throw new ValidationException("Дата релиза не может позже текущей даты");
        }
        if (film.getDuration() < 1) {
            log.info("Продолжительность: " + film.getDuration());
            throw new ValidationException("Некорректная продолжительность фильма");
        }
    }
}
