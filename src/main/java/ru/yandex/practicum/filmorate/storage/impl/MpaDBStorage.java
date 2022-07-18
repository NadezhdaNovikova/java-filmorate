package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDBStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(int id) {
        final String sqlQuery = "SELECT * FROM FILMORATE.PUBLIC.MPA WHERE MPA_ID = ?";
        final List<Mpa> mpa = jdbcTemplate.query(sqlQuery, MpaDBStorage::makeMpa, id);
        if (mpa.isEmpty()) {
            throw new EntityNotFoundException(String.format("Рейтинг с id = %s не найден", id));
        }
        return mpa.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        final String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery, MpaDBStorage::makeMpa);
    }

    private static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }
}