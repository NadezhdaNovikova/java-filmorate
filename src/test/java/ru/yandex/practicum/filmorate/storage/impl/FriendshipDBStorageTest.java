package ru.yandex.practicum.filmorate.storage.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
class FriendshipDBStorageTest {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public FriendshipDBStorageTest(UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    @Test
    void addFriend() {
        User user = new User("mail@mail.ru", "userLogin", "userName", LocalDate.of(1946, 8, 20));
        User user2 = new User("mailNew@mail.ru","userLoginNew", "userNameNew", LocalDate.of(1946, 8,20));
        Friendship friendship = new Friendship(user, user2);
        userStorage.add(user);
        userStorage.add(user2);

        assertTrue(friendshipStorage.getUserFriends(user.getId()).isEmpty());

        friendshipStorage.addFriend(friendship);

        assertFalse(friendshipStorage.getUserFriends(user.getId()).isEmpty());
        assertTrue(friendshipStorage.getUserFriends(user2.getId()).isEmpty());
        assertEquals(user2.getId(), friendshipStorage.getUserFriends(user.getId()).get(0));
    }

    @Test
    void removeFriend() {
        User user = new User("mailRF@mail.ru", "userLoginRF", "userName", LocalDate.of(1946, 8, 20));
        User user2 = new User("mailNewRF@mail.ru","userLoginNewRF", "userNameNew", LocalDate.of(1946, 8,20));
        Friendship friendship = new Friendship(user2, user);
        userStorage.add(user);
        userStorage.add(user2);

        assertTrue(friendshipStorage.getUserFriends(user2.getId()).isEmpty());
        friendshipStorage.addFriend(friendship);
        assertFalse(friendshipStorage.getUserFriends(user2.getId()).isEmpty());
        friendshipStorage.removeFriend(friendship);
        assertTrue(friendshipStorage.getUserFriends(user2.getId()).isEmpty());

    }

    @Test
    void getUserFriends() {
        User user = new User("mailGet@mail.ru", "userLoginGet", "userName", LocalDate.of(1946, 8, 20));
        User user2 = new User("mailGet2@mail.ru","userLoginGet2", "userNameNew", LocalDate.of(1946, 8,20));
        Friendship friendship = new Friendship(user, user2);
        userStorage.add(user);
        userStorage.add(user2);

        assertTrue(friendshipStorage.getUserFriends(user.getId()).isEmpty());

        friendshipStorage.addFriend(friendship);

        assertEquals(1, friendshipStorage.getUserFriends(user.getId()).size());
    }

    @Test
    void mutualFriends() {
        User user = new User("mailM1@mail.ru", "userLoginM1", "userName", LocalDate.of(1946, 8, 20));
        User user2 = new User("mailM2@mail.ru", "userLoginM2", "userName", LocalDate.of(1946, 8, 20));
        User user3 = new User("mailM3@mail.ru", "userLoginM3", "userName", LocalDate.of(1946, 8, 20));
        Friendship friendship1_3 = new Friendship(user, user3);
        Friendship friendship2_3 = new Friendship(user2, user3);
        userStorage.add(user);
        userStorage.add(user2);
        userStorage.add(user3);

        assertTrue(friendshipStorage.mutualFriends(user.getId(), user2.getId()).isEmpty());

        friendshipStorage.addFriend(friendship1_3);
        friendshipStorage.addFriend(friendship2_3);

        assertEquals(1, friendshipStorage.mutualFriends(user.getId(), user2.getId()).size());
    }
}