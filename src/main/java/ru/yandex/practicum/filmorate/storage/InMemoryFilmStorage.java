package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void delete(Film film) {

    }


    @Override
    public void addLike() {

    }

    @Override
    public void deleteLike() {

    }

    @Override
    public List<Film> getTenPopularFilms() {
        return null;
    }
}
