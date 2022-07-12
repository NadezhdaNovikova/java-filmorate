package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FilmDBStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Film> getAll() {
        return null;
    }

    @Override
    public long add(Film film) {
            jdbcTemplate.update(
                    "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATA, DURATION, MPA_ID) values (?,?,?,?,?)",
                    film.getMpa(),
                    film.getName(),
                    film.getReleaseDate(),
                    film.getDescription(),
                    film.getDuration()
            );
            return film.getId();
        }


    @Override
    public void change(Film entity) {

    }

    @Override
    public void delete(Film entity) {

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
}