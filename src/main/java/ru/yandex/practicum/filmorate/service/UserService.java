package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserStorage userStorage;
    private final Map<Long, User> users = new HashMap<>();
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9]{1,}" + "((\\.|\\_|-{0,1})[a-zA-Z0-9]{1,})*" + "@"
            + "[a-zA-Z0-9]{1,}" + "((\\.|\\_|-{0,1})[a-zA-Z0-9]{1,})*" + "\\.[a-zA-Z]{2,}$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean validate(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @RequestMapping("/users")

    @GetMapping("/users")
    public Collection<User> getAll() {
        Collection<User> allUsers = userStorage.getAll();
        log.info("Текущее количество пользователей: {}", allUsers.size());
        return allUsers;
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        userCreateValidate(user);
        userStorage.add(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        userUpdateValidate(user);
        userStorage.change(user);
        return user;
    }

    private void userCreateValidate(User user) {
        userGeneralValidate(user);
        for (Long i : users.keySet()) {
            if (users.get(i).getLogin().equals(user.getLogin())) {
                log.info("Логин принадлежит пользователю " + users.get(i));
                throw new ValidationException("Пользователь с логином  " + user.getLogin() + " уже существует");
            }
            if (users.get(i).getEmail().equals(user.getEmail())) {
                log.info("Email принадлежит пользователю " + users.get(i));
                throw new ValidationException("Пользователь с email  " + user.getEmail() + " уже существует");
            }
        }
    }

    private void userUpdateValidate(User user) {
        userGeneralValidate(user);
        if (user.getId() < 1 | !users.containsKey(user.getId())) {
            log.info("id: " + user.getId());
            throw new ValidationException("Обновление невозможно. Некорректный id");
        }
        if (!user.getEmail().equals(users.get(user.getId()).getEmail())) {
            log.info("Email: " + user.getEmail());
            for (Long i : users.keySet()) {
                if (users.get(i).getEmail().equals(user.getEmail())) {
                    log.info("Email принадлежит пользователю " + users.get(i));
                    throw new ValidationException("Желаемый email занят");
                }
                if (users.get(i).getLogin().equals(user.getLogin())) {
                    log.info("Логин принадлежит пользователю " + users.get(i));
                    throw new ValidationException("Желаемый логин занят");
                }
            }
        }
    }

    private void userGeneralValidate(User user) {
        if (isNull(user)) {
            log.info("Получен запрос: " + user);
            throw new ValidationException("Получен пустой запрос");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.info("Email: " + user.getEmail());
            throw new ValidationException("Email отсутствует");
        }
        if (!validate(user.getEmail())) {
            log.info("Email: " + user.getEmail());
            throw new ValidationException("Некорректный формат email");
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
}
