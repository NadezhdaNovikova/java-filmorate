package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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
        final String sqlQuery = "select * from MPA where MPA_ID = ?";
        final List<Mpa> mpa = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs), id);
        return mpa.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        final String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(
                rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }
}