package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class MpaDBStorageTest {

    MpaDBStorage mpaDBStorage;

    @Autowired
    public MpaDBStorageTest(MpaDBStorage mpaDBStorage) {
        this.mpaDBStorage = mpaDBStorage;
    }

    @Test
    void getById() {
        Mpa mpa = mpaDBStorage.getById(1);
        assertEquals("G", mpa.getName());
    }

    @Test
    void getAll() {
        List<Mpa> filmMpaList = mpaDBStorage.getAll();
        assertEquals(5, filmMpaList.size());
        assertEquals('G', filmMpaList.get(0));
    }
}