package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDBStorageTest {

    private final UserStorage userStorage;

    @Autowired
    public UserDBStorageTest(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Test
    void addAndGetById() {
        User testUser1 = new User("Add@mail.ru", "LoginAdd", "userName", LocalDate.of(1946, 8, 20));
        userStorage.add(testUser1);
        Optional<User> userOptional = userStorage.getById(testUser1.getId());
        assertNotNull(userOptional);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("email", "Add@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "LoginAdd")
                                .hasFieldOrPropertyWithValue("name", "userName")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1946, 8, 20))
                );

        final int idUser = -1;

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> userStorage.getById(idUser));
        assertEquals(String.format("Пользователь с id = %s не найден", idUser), ex.getMessage());
    }


    @Test
    void change() {
        User testUser2 = new User("test2@email.ru", "testLogin2", "testName2", LocalDate.of(1980, 8, 16));
        userStorage.add(testUser2);

        User userNew = new User(testUser2.getId(),"Change@mail.ru","Change", "userNameNew", LocalDate.of(1946, 8,20));
        userStorage.change(userNew);
        Optional<User> userOptional = userStorage.getById(testUser2.getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("email", "Change@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "Change")
                                .hasFieldOrPropertyWithValue("name", "userNameNew")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1946, 8,20))
                );
    }

    @Test
    void delete() {
        User userForDelete = new User("testdelete@email.ru", "testDelete", "testName2", LocalDate.of(1980, 8, 16));
        userStorage.add(userForDelete);
        Long userDeleteId = userForDelete.getId();
        User userAdded = userStorage.getById(userDeleteId).get();

        assertEquals(userForDelete.getLogin(), userAdded.getLogin());

        userStorage.delete(userForDelete);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> userStorage.getById(userDeleteId));
        assertEquals(String.format("Пользователь с id = %s не найден", userDeleteId), ex.getMessage());
    }


    @Test
    void getAllUsers() {
        User testUserAll = new User("testAll@email.ru", "testLoginAll", "testNameAll", LocalDate.of(1980, 8, 16));

        List<User> userListBeforeAdd = userStorage.getAll();

        userStorage.add(testUserAll);

        List<User> userListAfterAdd = userStorage.getAll();

        assertEquals(userListBeforeAdd.size() + 1, userListAfterAdd.size());
    }
}