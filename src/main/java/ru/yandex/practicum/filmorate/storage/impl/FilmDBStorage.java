package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDBStorage mpaDBStorage;
    private final GenreDBStorage genreDBStorage;

    public FilmDBStorage(JdbcTemplate jdbcTemplate, MpaDBStorage mpaDBStorage, GenreDBStorage genreDBStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDBStorage = mpaDBStorage;
        this.genreDBStorage = genreDBStorage;
    }

    @Override
    public Optional<Film> getById(long id) {
        final String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";

        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (films.isEmpty()) {
            throw new EntityNotFoundException(String.format("Фильм с id = %s не найден", id));
        }
        Film film = films.get(0);
        Set<Genre> genres = genreDBStorage.getFilmGenres(film.getId());
        film.setGenres(genres);
        return Optional.of(film);
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "SELECT * FROM FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        for (Film film : films) {
            film.setGenres(genreDBStorage.getFilmGenres(film.getId()));
        }
        return films;
    }

    Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                mpaDBStorage.getById(rs.getInt("MPA_ID")));
    }

    @Override
    public Optional<Film>  add(Film film) {
        String sqlQuery = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            stmt.setDate(3, Date.valueOf(releaseDate));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        final Set<Genre> filmGenres = film.getGenres();

        if (filmGenres != null) {
            final String addGenres = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            filmGenres.forEach(x -> jdbcTemplate.update(addGenres, film.getId(), x.getId()));
        }
        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<Film> change(Film film) {
            final String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                    "MPA_ID = ? WHERE FILM_ID = ?";

            jdbcTemplate.update(sql
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getId());

            final String deleteGenres = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(deleteGenres, film.getId());

            final Set<Genre> filmGenres = film.getGenres();

            if (filmGenres != null) {
                final String addGenres = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
                filmGenres.forEach(x -> jdbcTemplate.update(addGenres, film.getId(), x.getId()));
            }
        return getById(film.getId());
    }

    @Override
    public void delete(Film film) {
        final String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        final String sqlQuery = "INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);

    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        final String sqlQuery = "DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        final String sqlQuery = "SELECT * FROM FILMS F LEFT JOIN (SELECT FILM_ID, COUNT(*) LIKES_COUNT FROM LIKES"
                + " GROUP BY FILM_ID) L ON F.FILM_ID = L.FILM_ID LEFT JOIN MPA ON F.MPA_ID = MPA.MPA_ID"
                + " ORDER BY L.LIKES_COUNT DESC LIMIT ?";

        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, count);
        for (Film film : films) {
            film.setGenres(genreDBStorage.getFilmGenres(film.getId()));
        }
        return films;
    }
}