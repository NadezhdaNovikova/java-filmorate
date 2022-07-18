package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class LikeDBStorage implements LikeStorage {

    JdbcTemplate jdbcTemplate;

    public LikeDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Like like) {
        final String sqlQuery = "INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery,
                like.getUser().get().getId(),
                like.getFilm().get().getId());
    }

    @Override
    public void removeLike(Like like) {
        final String sqlQuery = "DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, like.getUser().get().getId(), like.getFilm().get().getId());
    }

    @Override
    public List<Long> getPopularFilms(Integer count) {
            SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("SELECT F.FILM_ID FROM FILMS F LEFT JOIN " +
                    "(SELECT FILM_ID, COUNT(*) LIKES_COUNT FROM LIKES"
                    + " GROUP BY FILM_ID) L ON F.FILM_ID = L.FILM_ID LEFT JOIN MPA ON F.MPA_ID = MPA.MPA_ID"
                    + " ORDER BY L.LIKES_COUNT DESC LIMIT ?", count);

            final List<Long> filmsId = new LinkedList<>();
            while (sqlQuery.next()) {
                Long film = sqlQuery.getLong("FILM_ID");
                filmsId.add(film);
            }
            return filmsId;
    }
}