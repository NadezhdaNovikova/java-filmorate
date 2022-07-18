package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.FilmDBStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmDBStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAll();
        List<Film> updatedFilms = new ArrayList<>();
        for (Film film : films) {
            film.setGenres(genreStorage.getFilmGenres(film.getId()));
            updatedFilms.add(film);
        }
        return updatedFilms;
    }

    public Optional<Film> getById(Long id) {
        Optional<Film> film = filmStorage.getById(id);
        Set<Genre> genres = genreStorage.getFilmGenres(id);
        film.get().setGenres(genres);
        return film;
    }

    public Optional<Film>  createFilm(Film film) {
        filmStorage.add(film);
        return Optional.of(film);
    }

    public Optional<Film> updateFilm(Film film) {
        getById(film.getId());
        filmStorage.change(film);
        return filmStorage.change(film);
    }

    public void addLike(Long filmId, Long userId) {
        getById(filmId);
        Like like = new Like(userStorage.getById(userId), getById(filmId));
        if (userStorage.getById(userId).isPresent()) {
            likeStorage.addLike(like);
        } else {
            throw new EntityNotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    public void removeLike(Long filmId, Long userId) {
        getById(filmId);
        Like like = new Like(userStorage.getById(userId), getById(filmId));
        if (userStorage.getById(userId).isPresent()) {
            likeStorage.removeLike(like);
        } else {
            throw new EntityNotFoundException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    public List<Optional<Film>> getPopularFilms(Integer count) {
        List<Optional<Film>> films = new LinkedList<>();
        for (Long id: likeStorage.getPopularFilms(count)) {
            films.add(getById(id));
        }
        return films;
    }
}