package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureTestDatabase
class LikeDBStorageTest {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    LikeDBStorageTest(UserStorage userStorage, FilmStorage filmStorage, LikeStorage likeStorage, MpaStorage mpaStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.mpaStorage = mpaStorage;
    }

    @Test
    void addLike() {
        Film filmAddLike = new Film("First", "FilmDescription", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(1));
        Film film2 = new Film("Second", "FilmDescription2", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(1));

        filmStorage.add(filmAddLike);
        filmStorage.add(film2);

        User userAddLike = new User("userAddLike@mail.ru", "userAddLike", "userName", LocalDate.of(1946, 8, 20));
        Like like= new Like(userAddLike, film2);
        userStorage.add(userAddLike);

        likeStorage.addLike(like);

        assertEquals(film2.getId(), likeStorage.getPopularFilms(5).get(0));
    }

    @Test
    void removeLike() {
        Film filmRemoveLike = new Film("Film", "FilmDescription", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(1));
        Film film2 = new Film("Film2", "FilmDescription2", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(1));

        filmStorage.add(filmRemoveLike);
        filmStorage.add(film2);

        User userRemoveLike = new User("userRemoveLike@mail.ru", "userRemoveLike", "userName", LocalDate.of(1946, 8, 20));
        Like like= new Like(userRemoveLike, film2);
        userStorage.add(userRemoveLike);


        Long id = likeStorage.getPopularFilms(5).get(0);

        likeStorage.addLike(like);

        assertEquals(film2.getId(), likeStorage.getPopularFilms(5).get(0));

        likeStorage.removeLike(like);

        assertEquals(id, likeStorage.getPopularFilms(5).get(0));
    }

    @Test
    void getPopularFilms() {
        Film film1 = new Film("First", "FilmDescription", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(1));
        Film film2 = new Film("Second", "FilmDescription2", LocalDate.of(2020, 2, 22), 120, mpaStorage.getById(1));
        filmStorage.add(film1);
        filmStorage.add(film2);

        assertFalse(likeStorage.getPopularFilms(10).isEmpty());
    }
}