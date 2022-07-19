package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class MpaDBStorageTest {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaDBStorageTest(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Test
    void getById() {
        Mpa mpa = mpaStorage.getById(1);
        assertEquals("G", mpa.getName());
    }

    @Test
    void getAll() {
        List<Mpa> filmMpaList = mpaStorage.getAll();
        assertEquals(5, filmMpaList.size());
        assertEquals("G", filmMpaList.get(0).getName());
    }
}