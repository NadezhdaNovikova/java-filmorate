package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User add(User user) {
        String sqlQuery = "insert into USERS(EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public Optional<User> getById(long id) {
        final String sqlQuery = "select * from USERS where USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDBStorage::makeUser, id);
        if (users.isEmpty()) {
            throw new EntityNotFoundException(String.format("Пользователь с id = %s не найден", id));
        }
        return Optional.ofNullable(users.get(0));
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                (rs.getDate("BIRTHDAY")).toLocalDate());
    }

    @Override
    public List<User> getAll() {
        final String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, UserDBStorage::makeUser);
    }

    @Override
    public void change(User user) {
        final String sqlQuery = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
    }

    @Override
    public void delete(User user) {
        final String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getId());
    }

    public void addFriend(long userId, long friendId) {
        final String sql = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        final String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getUserFriends(long userId) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, UserDBStorage::makeUser, userId);
    }

    public List<User> mutualFriends(long userId, long otherId) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID IN (select u.FRIEND_ID from FRIENDS u, FRIENDS o " +
                "where u.FRIEND_ID = o.FRIEND_ID " +
                "and u.USER_ID = ? " +
                "and o.USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, UserDBStorage::makeUser, userId, otherId);
    }

}