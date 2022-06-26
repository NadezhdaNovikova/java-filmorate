package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {
    @Override
    public void add(){
//TODO
    }

    @Override
    public void change() {
//TODO
    }

    @Override
    public void delete() {
//TODO
    }

    @Override
    public void addFriend(long userId, long friendId) {

    }

    @Override
    public void removeFriend(long userId, long friendId) {

    }

    @Override
    public List<User> mutualFriends(long userId1, long userId2) {

        return null;
    }

    public void deleteFriend (long userId, long friendId) {

    }
}
