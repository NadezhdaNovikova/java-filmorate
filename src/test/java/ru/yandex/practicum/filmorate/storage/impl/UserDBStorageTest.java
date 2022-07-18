package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class UserDBStorageTest {
/*
    private final UserStorage userStorage;

    @Test
    @Order(1)
    void addAndGetById() {
        User user = new User("mail@mail.ru", "userLogin", "userName", LocalDate.of(1946, 8, 20));
        userStorage.add(user);
        Optional<User> userOptional = userStorage.getById(1);
        assertNotNull(userOptional);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("email", "mail@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "userLogin")
                                .hasFieldOrPropertyWithValue("name", "userName")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1946, 8, 20))
                );
    }


    @Test
    @Order(2)
    void getAllUsers() {
        List<User> users = userStorage.getAll();
            assertNotNull(users);
            assertEquals(1, users.size());
        }


    @Test
    @Order(3)
    void change() {
        User userNew = new User(1L,"mailNew@mail.ru","userLoginNew", "userNameNew", LocalDate.of(1946, 8,20));
        userStorage.change(userNew);
        Optional<User> userOptional = userStorage.getById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("email", "mailNew@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "userLoginNew")
                                .hasFieldOrPropertyWithValue("name", "userNameNew")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1946, 8,20))
                );
    }*/
}