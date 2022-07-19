package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class GenreDBStorageTest {

    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public GenreDBStorageTest(GenreStorage genreStorage, MpaStorage mpaStorage, FilmStorage filmStorage) {
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.filmStorage = filmStorage;
    }

    @Test
    void getById() {
        Genre genre = genreStorage.getById(1).get();
        assertEquals("Комедия", genre.getName());
    }

    @Test
    void getAll() {
        List<Genre> filmGenreList = genreStorage.getAll();
        assertEquals(6, filmGenreList.size());
    }

    @Test
    void getFilmGenres() {
        Film filmGenre = new Film("Film", "FilmDescription", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(1));
        Set<Genre> genres = new HashSet<>();
        genres.add(genreStorage.getById(1).get());
        genres.add(genreStorage.getById(2).get());
        genres.add(genreStorage.getById(3).get());
        genres.add(genreStorage.getById(2).get());
        genres.add(genreStorage.getById(1).get());

        filmGenre.setGenres(genres);
        filmStorage.add(filmGenre);
        Set<Genre> filmGenres = genreStorage.getFilmGenres(filmGenre.getId());
        assertEquals(3, filmGenres.size());
    }
}