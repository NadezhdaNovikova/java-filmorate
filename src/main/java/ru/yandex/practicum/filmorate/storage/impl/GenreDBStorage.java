package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
public class GenreDBStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        return new LinkedList<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs)));
    }

    @Override
    public Optional<Genre> getById(long id) {
        final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
        if (genres.isEmpty()) {
            throw new EntityNotFoundException(String.format("Жанр с id = %s не найден", id));
        }
        return Optional.ofNullable(genres.get(0));
    }

    Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }

    Set<Genre> getFilmGenres(long id) {
        final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID IN " +
                "(SELECT FILM_GENRES.GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?) ORDER BY GENRE_ID";
        final Set<Genre> genres = new LinkedHashSet<>();
        genres.addAll(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id));
        return genres;
    }

    public void setFilmGenres(Film film) {
        Set<Genre> filmGenres = film.getGenres();

        if (filmGenres.size() != 0) {
            log.info("SET        " + filmGenres.size());
            final String sqlQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            filmGenres.forEach(x -> jdbcTemplate.update(sqlQuery, film.getId(), x.getId()));
        }
    }
}