package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import util.idUserGenerator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@RestController
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private final List<String> emails = new ArrayList<>();
    private final List<String> logins =new ArrayList<>();
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9]{1,}" + "((\\.|\\_|-{0,1})[a-zA-Z0-9]{1,})*" + "@"
            + "[a-zA-Z0-9]{1,}" + "((\\.|\\_|-{0,1})[a-zA-Z0-9]{1,})*"+"\\.[a-zA-Z]{2,}$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean validate(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @RequestMapping("/users")

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        if (isNull(user)) {
            throw new ValidationException("Получен пустой запрос");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email отсутствует");
        }
        if (!validate(user.getEmail())) {
            throw new ValidationException("Некорректный формат email");
        }
        if (emails.contains(user.getEmail())) {
            log.info(String.valueOf(emails));
            throw new ValidationException("Пользователь с email  " + user.getEmail() + " уже существует");
        }
        if (isNull(user.getLogin())) {
            throw new ValidationException("Не указан логин");
        }
        if (logins.contains(user.getLogin())) {
            throw new ValidationException("Пользователь с логином  " + user.getLogin() + " уже существует");
        }
        if (!user.getLogin().toLowerCase().matches("[a-z]+")) {
            throw new ValidationException("Логин должен содержать только буквы латинского алфавита");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть больше текущей");
        }
        if (isNull(user.getName()) | user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info(user.getLogin() + " " + user.getName());
        }
        user.setId(idUserGenerator.getId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        logins.add(user.getLogin());
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        if (isNull(user)) {
            throw new ValidationException("Получен пустой запрос");
        }
        if (user.getId() < 1) {
            throw new ValidationException("Обновление невозможно. Некорректный id");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email отсутствует");
        }
        if (!validate(user.getEmail())) {
            throw new ValidationException("Некорректный формат email");
        }
        if (emails.contains(user.getEmail()) & !user.getEmail().equals(users.get(user.getId()).getEmail())) {
            throw new ValidationException("Желаемый email занят");
        }
        if (isNull(user.getLogin())) {
            throw new ValidationException("Не указан логин");
        }
        if (!user.getLogin().toLowerCase().matches("[a-z]+")) {
            throw new ValidationException("Логин должен содержать только буквы латинского алфавита");
        }
        if (logins.contains(user.getLogin()) & !user.getLogin().equals(users.get(user.getId()).getLogin())) {
            throw new ValidationException("Желаемый логин занят");
        }
        if (isNull(user.getName())) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть больше текущей");
        }
        if (isNull(user.getId())) {
            user.setId(idUserGenerator.getId());
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            logins.add(user.getLogin());

        } else if (users.containsKey(user.getId())) {
            emails.remove(users.get(user.getId()).getEmail());
            logins.remove(users.get(user.getId()).getLogin());
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            logins.add(user.getLogin());
        } else {
            throw new ValidationException("Указан некорректный id пользователя");
        }
        return user;
    }

}