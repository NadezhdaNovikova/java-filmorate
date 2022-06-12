package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final Map<String, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/users")

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws InvalidEmailException {
        if(user.getEmail()==null || user.getEmail().isBlank()){
            throw new InvalidEmailException("Email отсутствует");
        }
        if(users.containsKey(user.getEmail())){
            throw new UserAlreadyExistException("Пользователь с email  "+user.getEmail()+" уже существует");
        }
        users.put(user.getEmail(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws InvalidEmailException {
        if(user.getEmail()==null || user.getEmail().isBlank()){
            throw new InvalidEmailException("Email отсутствует");
        }
        if(users.containsKey(user.getEmail())){
            users.put(user.getEmail(), user);
        }
        return user;
    }

}