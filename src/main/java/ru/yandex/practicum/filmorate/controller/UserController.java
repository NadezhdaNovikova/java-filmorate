package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users")

    @GetMapping("/users")
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        userService.createUser(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping
    public void deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
    }

    @GetMapping("/users/{id}")
    public Optional<User> getById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id,
                          @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Long id,
                             @PathVariable("friendId") Long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable("id") Long id,
                                    @PathVariable("otherId") Long otherId) {
        return userService.mutualFriends(id, otherId);
    }
}