package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    void addFriend(long userId, long friendId); //добавление друзей  если Лена стала другом Саши, то это значит,
    // что Саша теперь друг Лены.
    void removeFriend(long userId, long friendId); //удалить друга
    List<User> mutualFriends(long userId1, long userId2); //список общих друзей
}
