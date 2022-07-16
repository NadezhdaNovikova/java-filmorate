package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage extends Storage<User> {

    @Override
    Optional<User> getById(long id);

    @Override
    List<User> getAll();

    @Override
    User add(User user);

    @Override
    Optional<Film> change(User user);

    @Override
    void delete(User user);

    void addFriend(long userId, long friendId);          // Добавить друга
    void removeFriend(long userId, long friendId);       // Удалить друга
    List<User> getUserFriends(long userId);              // Получить список друзей
    List<User> mutualFriends(long id, long otherId);     // Получить список общих друзей
}