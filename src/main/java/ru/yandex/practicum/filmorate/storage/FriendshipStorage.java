package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage{
    void addFriend(Friendship friendship);                   // Добавить друга
    void removeFriend(Friendship friendship);                // Удалить друга
    List<Long> getUserFriends(long userId);                  // Получить список друзей
    List<Long> mutualFriends(long userId, long otherId);     // Получить список общих друзей
}