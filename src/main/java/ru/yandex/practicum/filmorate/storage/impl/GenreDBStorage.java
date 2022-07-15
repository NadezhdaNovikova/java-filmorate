package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDBStorage implements GenreStorage {

    private JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
            String sqlQuery = "SELECT * FROM GENRES";
            final List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
            return genres;
    }

    @Override
    public Optional<Genre> getById(long id) {
        final String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
        return Optional.ofNullable(genres.get(0));
    }

    Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

    Set<Genre> getFilmGenres(long id) {
        final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID IN (SELECT FILM_GENRES.GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        final Set<Genre> genres = new HashSet<>();
        genres.addAll(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id));
        return genres;
    }
}