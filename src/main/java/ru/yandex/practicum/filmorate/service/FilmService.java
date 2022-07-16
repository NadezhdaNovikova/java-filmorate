package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.FilmDBStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDBStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final LocalDate VALID_DATE_FILM = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmDBStorage filmStorage, UserDBStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Optional<Film> getById(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(filmStorage.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id = %s не найден", id))));
    }

    public Film createFilm(Film film) {
        filmValidate(film);
        filmStorage.add(film);
        return film;
    }

    public Optional<Film> updateFilm(Film film) {
        getById(film.getId());
        filmValidate(film);
        filmStorage.change(film);
        return filmStorage.change(film);
    }

    public void addLike(Long id, Long userId) {
        getById(id);
        if (userStorage.getById(userId).isPresent()) {
            filmStorage.addLike(id, userId);
        } else {
            throw new EntityNotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    public void removeLike(Long id, Long userId) {
        getById(id);
        if (userStorage.getById(userId).isPresent()) {
            filmStorage.removeLike(id, userId);
        } else {
            throw new EntityNotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    private void filmValidate(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Название фильма: " + film.getName());
            throw new ValidationException("Название фильма отсутствует");
        }
        if (film.getDescription().length() > 200) {
            log.info("Длина описания фильма: " + film.getDescription().length() + " символов.");
            throw new ValidationException("Описание фильма должно не превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(VALID_DATE_FILM)) {
            log.info("Дата релиза: " + film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getReleaseDate().isAfter(LocalDate.now())) {
            log.info("Дата релиза: " + film.getReleaseDate() + ". Текущая дата " + LocalDate.now());
            throw new ValidationException("Дата релиза не может позже текущей даты");
        }
        if (film.getDuration() < 1) {
            log.info("Продолжительность: " + film.getDuration());
            throw new ValidationException("Некорректная продолжительность фильма");
        }
    }
}