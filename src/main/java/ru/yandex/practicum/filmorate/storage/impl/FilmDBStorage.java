package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    MpaDBStorage mpaDBStorage;

    public FilmDBStorage(JdbcTemplate jdbcTemplate, MpaDBStorage mpaDBStorage ) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDBStorage = mpaDBStorage;
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Film> getAll() {
        final Map<Long, Set<Genre>> filmsGenres = getAllFilmGenres();
        final String sql = "SELECT * FROM FILMS LEFT JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID";
        return jdbcTemplate.query(sql, (rs, numRow) -> {
            final Long filmId = rs.getLong("film_id");
            return makeFilm(rs, filmsGenres.get(filmId));
        });
    }

    private Film makeFilm(ResultSet rs, Set<Genre> filmsGenres) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATA").toLocalDate(),
                rs.getInt("DURATION"),
                mpaDBStorage.getById(rs.getInt("MPA_ID")),
                filmsGenres);
    }

    @Override
    public Film add(Film film) {
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
        film.setId(keyHolder.getKey().longValue());
        return film;
    }


    @Override
    public void change(Film film) {
        final String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?"
                + " WHERE FILM_ID = ?";

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
    }

    @Override
    public void delete(Film film) {
        final String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public void addLike(Long id, Long userId) {
     //   super.data.get(id).addLike(userId);
    }

    @Override
    public void removeLike(Long id, Long userId) {
        //super.data.get(id).removeLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return null;
    }

    private Map<Long, Set<Genre>> getAllFilmGenres() {
        final String sql = "SELECT * FROM FILM_GENRES LEFT JOIN GENRES ON GENRES.GENRE_ID = FILM_GENRES.GENRE_ID";
        final Map<Long, Set<Genre>> filmGenres = new HashMap<>();

        jdbcTemplate.query(sql, rs -> {
            final Long filmId = rs.getLong("film_id");
            filmGenres.getOrDefault(filmId, new HashSet<>()).add(new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME")));
        });

        return filmGenres;
    }

    public Film addGenres(Film film){
        List <Genre> genres = film.getGenres().stream().toList();
        if(!genres.isEmpty()) {
            for (int i = 0; i < film.getGenres().size(); i++) {
                String sqlQuery = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values (?, ?) on conflict do nothing";

                jdbcTemplate.update(sqlQuery
                        , film.getId()
                        , genres.get(i).getId());
            }
        }
        return film;
    }
}