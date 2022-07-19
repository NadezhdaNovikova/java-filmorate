package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDBStorageTest {

    MpaDBStorage mpaDBStorage;

    @Test
    void getById() {
        Mpa mpa = mpaDBStorage.getById(1);
        assertEquals("G", mpa.getName());
    }

    @Test
    void getAll() {
        List<Mpa> filmMpaList = mpaDBStorage.getAll();
        assertEquals(6, filmMpaList.size());
        assertEquals('G', filmMpaList.get(0));
    }

    @Test
    void getFilmGenres() {
    }
}