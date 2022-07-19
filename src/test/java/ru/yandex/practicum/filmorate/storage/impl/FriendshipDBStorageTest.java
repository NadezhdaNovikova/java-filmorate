package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendshipDBStorageTest {

    private final UserStorage userStorage;
    private final FriendshipDBStorage friendshipDBStorage;

    @Test
    @Order(1)
    void addFriend() {
        User user = new User("mail@mail.ru", "userLogin", "userName", LocalDate.of(1946, 8, 20));
        User user2 = new User("mailNew@mail.ru","userLoginNew", "userNameNew", LocalDate.of(1946, 8,20));
        Friendship friendship = new Friendship(user, user2);
        userStorage.add(user);
        userStorage.add(user2);
        assertTrue(friendshipDBStorage.getUserFriends(user.getId()).isEmpty());
        friendshipDBStorage.addFriend(friendship);
        assertFalse(friendshipDBStorage.getUserFriends(user.getId()).isEmpty());
        assertEquals(2L, friendshipDBStorage.getUserFriends(user.getId()).get(0));
    }

    @Test
    @Order(2)
    void removeFriend() {
        User user = new User("mailRF@mail.ru", "userLoginRF", "userName", LocalDate.of(1946, 8, 20));
        User user2 = new User("mailNewRF@mail.ru","userLoginNewRF", "userNameNew", LocalDate.of(1946, 8,20));
        Friendship friendship = new Friendship(user, user2);

        assertFalse(friendshipDBStorage.getUserFriends(1L).isEmpty());
        friendshipDBStorage.removeFriend(friendship);
        assertTrue(friendshipDBStorage.getUserFriends(1L).isEmpty());
    }

    @Test
    @Order(3)
    void getUserFriends() {

    }

    @Test
    @Order(4)
    void mutualFriends() {
    }
}