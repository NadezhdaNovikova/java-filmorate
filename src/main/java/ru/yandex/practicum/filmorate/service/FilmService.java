package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class FilmService {
    @Autowired
    InMemoryFilmStorage filmStorage;

    public Collection<Film> getAllFilms() {
return filmStorage.getAll();
    }


    public Film createFilm(Film film) {
        filmValidate(film);
filmStorage.add(film);
        return film;
    }


    public Film updateFilm(Film film) {
        filmValidate(film);
        filmStorage.change(film);
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
