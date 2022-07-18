package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public List<User> getAll() {
        List<User> users = userStorage.getAll();
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

    public User createUser(@RequestBody User user) {
        userValidateAlreadyExistsEmailAndLogin(user);
        userStorage.add(user);
        return user;
    }

    public User updateUser(@RequestBody User user) {
        getById(user.getId());
        userValidateAlreadyExistsEmailAndLogin(user);
        userStorage.change(user);
        return user;
    }

    public Optional<User> getById( Long id) {
        return userStorage.getById(id);
    }

    public void addFriend(Long id, Long friendId) {
        getById(id);
        getById(friendId);
        Friendship friendship = new Friendship(getById(id), getById(friendId));
        friendshipStorage.addFriend(friendship);
    }

    public void removeFriend(Long id, Long friendId) {
        getById(id);
        getById(friendId);
        Friendship friendship = new Friendship(getById(id), getById(friendId));
        friendshipStorage.removeFriend(friendship);
    }

    public List<Optional<User>> getUserFriends(Long id) {
        getById(id);
        List<Optional<User>> friends = new ArrayList<>();
        for (Long friendId :friendshipStorage.getUserFriends(id)
             ) {
            friends.add(getById(friendId));
        }
        return friends;
    }

    public List<Optional<User>>  mutualFriends(Long id, Long otherId) {
        getById(id);
        getById(otherId);
        List<Optional<User>> friends = new ArrayList<>();
        for (Long friendId :friendshipStorage.mutualFriends(id, otherId)
        ) {
            friends.add(getById(friendId));
        }
        return friends;
    }

    public void deleteUser(User user) {
        getById(user.getId());
        userStorage.delete(user);
    }

    private void userValidateAlreadyExistsEmailAndLogin(User user) {
        List<User> users = userStorage.getAll();
        for (User u : users) {
            if(!u.getId().equals(user.getId())) {
                if (u.getLogin().equals(user.getLogin())) {
                    log.info("Логин принадлежит пользователю " + u);
                    throw new UserAlreadyExistException(String.format("Пользователь с логином %s уже существует",
                            user.getLogin()));
                }
                if (u.getEmail().equals(user.getEmail())) {
                    log.info("Email принадлежит пользователю " + u);
                    throw new UserAlreadyExistException(String.format("Пользователь с email %s уже существует",
                            user.getEmail()));
                }
            }
        }
    }
}