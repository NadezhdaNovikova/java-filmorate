package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    void addFriend(long userId, long friendId);          // Добавить друга
    void removeFriend(long userId, long friendId);       // Удалить друга
    List<User> getUserFriends(long userId);              // Получить список друзей
    List<User> mutualFriends(long id, long otherId);     // Получить список общих друзей
}
