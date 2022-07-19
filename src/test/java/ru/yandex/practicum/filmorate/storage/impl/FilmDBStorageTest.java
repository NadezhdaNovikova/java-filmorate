package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDBStorageTest {

    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmDBStorageTest(FilmStorage filmStorage, MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
    }

    @Test
    void getById() {
        Film filmGet = new Film("FilmGet", "FilmGetDescription", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(3));
        filmStorage.add(filmGet);

        Optional<Film> filmOptional = filmStorage.getById(filmGet.getId());
        assertNotNull(filmOptional);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "FilmGet"));
    }

    @Test
    void getAll() {
        Film filmAll = new Film("FilmAll", "FilmAllDescription", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(3));
        filmStorage.add(filmAll);

        List<Film> filmListBeforeAdd = filmStorage.getAll();

        filmStorage.add(filmAll);

        List<Film> filmListAfterAdd = filmStorage.getAll();

        assertEquals(filmListBeforeAdd.size() + 1, filmListAfterAdd.size());
    }

    @Test
    void add() {
        Film film = new Film("Film", "FilmDescription", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(1));
        filmStorage.add(film);

        Optional<Film> filmOptional = filmStorage.getById(film.getId());
        assertNotNull(filmOptional);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "Film")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2020, 2, 22))
                );
    }

    @Test
    void change() {
        Film filmBeforeUpdate = new Film("Film", "FilmDescription", LocalDate.of(2020, 2, 22), 150, mpaStorage.getById(1));
        filmStorage.add(filmBeforeUpdate);

        Film filmToUpdate = new Film(filmBeforeUpdate.getId(),"FilmUpdated", "FilmDescription", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(2));
        filmStorage.change(filmToUpdate);

        Optional<Film> filmOptional = filmStorage.getById(filmBeforeUpdate.getId());
        assertNotNull(filmOptional);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "FilmUpdated")
                );
    }

    @Test
    void delete() {
        Film filmForDelete = new Film("FilmDel", "FilmDescription", LocalDate.of(2020, 2, 22), 150, mpaStorage.getById(1));

        filmStorage.add(filmForDelete);
        Long filmDeleteId = filmForDelete.getId();
        Film filmAdded = filmStorage.getById(filmDeleteId).get();

        assertEquals(filmForDelete.getName(), filmAdded.getName());

        filmStorage.delete(filmForDelete);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> filmStorage.getById(filmDeleteId));
        assertEquals(String.format("Фильм с id = %s не найден", filmDeleteId), ex.getMessage());
    }
}