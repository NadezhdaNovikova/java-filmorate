package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {

    public InMemoryFilmStorage(IdGenerator idGenerator) {
        super(idGenerator);
    }

    @Override
    public List<Film> getAll() {
        return super.getAll();
    }

    @Override
    public void addLike(Long id, Long userId) {
        super.data.get(id).addLike(userId);
    }

    @Override
    public void removeLike(Long id, Long userId) {
        super.data.get(id).removeLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return super.getAll().stream()
                .sorted(Comparator.comparing(Film::getRate))
                .limit(count)
                .collect(Collectors.toList());
    }
}