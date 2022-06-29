package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {

    @Override
    public void addFriend(long userId, long friendId) {
        super.checkEntityId(userId);
        super.checkEntityId(friendId);
        User user = super.data.get(userId);
        User friend = super.data.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        super.checkEntityId(userId);
        super.checkEntityId(friendId);
        User user = super.data.get(userId);
        User friend = super.data.get(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    @Override
    public List<User> getUserFriends(long userId) {
        super.checkEntityId(userId);
        Set<Long> friendsId = super.data.get(userId).getFriends();
        List<User> friends = new ArrayList<>();
        for (Long i : friendsId) {
            friends.add(super.data.get(i));
        }
        return friends;
    }

    @Override
    public List<User> mutualFriends(long userId, long otherId) {
        super.checkEntityId(userId);
        super.checkEntityId(otherId);
        Set<Long> userFriendsId = super.data.get(userId).getFriends();
        Set<Long> otherUserFriendsId = super.data.get(otherId).getFriends();
        List<User> mutualFriends = new ArrayList<>();

        for (Long i : userFriendsId) {
            if (otherUserFriendsId.contains(i)) {
                mutualFriends.add(super.data.get(i));
            }
        }
        return mutualFriends;
    }
}