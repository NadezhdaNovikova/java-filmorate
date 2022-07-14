package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDBStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@Service
@Validated
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9]{1,}" + "((\\.|\\_|-{0,1})[a-zA-Z0-9]{1,})*" + "@"
            + "[a-zA-Z0-9]{1,}" + "((\\.|\\_|-{0,1})[a-zA-Z0-9]{1,})*" + "\\.[a-zA-Z]{2,}$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Autowired
    public UserService(UserDBStorage userStorage) {
        this.userStorage = userStorage;
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

    public Optional<User> getById( Long id) throws EntityNotFoundException {
        return Optional.ofNullable(userStorage.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id = %s не найден", id))));
    }

    public void addFriend(Long id, Long friendId) {
        getById(id);
        getById(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        getById(id);
        getById(friendId);
        userStorage.removeFriend(id, friendId);
    }

    public List<User> getUserFriends(Long id) {
        getById(id);
        return userStorage.getUserFriends(id);
    }

    public List<User> mutualFriends(Long id, Long otherId) {
        getById(id);
        getById(otherId);
        return userStorage.mutualFriends(id, otherId);
    }

    public void deleteUser(User user) {
        getById(user.getId());
        userStorage.delete(user);
    }

    private void userGeneralValidate(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.info("Email: " + user.getEmail());
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        if (!validate(user.getEmail())) {
            log.info("Email: " + user.getEmail());
            throw new InvalidEmailException("Некорректный формат email.");
        }
        if (isNull(user.getLogin())) {
            log.info("Логин: " + user.getLogin());
            throw new ValidationException("Не указан логин");
        }
        if (!user.getLogin().toLowerCase().matches("[a-z]+")) {
            log.info("Логин: " + user.getLogin());
            throw new ValidationException("Логин должен содержать только буквы латинского алфавита");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения: " + user.getBirthday() + " Дата текущая " + LocalDate.now());
            throw new ValidationException("Дата рождения не может быть больше текущей");
        }
        if (isNull(user.getName()) | user.getName().isBlank()) {
            log.info("Имя пользователя не указано - " + user.getName());
            user.setName(user.getLogin());
            log.info("Имя пользователя установлено равным логину: " + user.getName());
        }
    }

    private void userValidateAlreadyExistsEmailAndLogin(User user) {
        List<User> users = userStorage.getAll();
        userGeneralValidate(user);
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

    private boolean validate(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}