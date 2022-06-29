package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {
    private final TreeSet<Film> sortedFilms = new TreeSet<>(Comparator.comparing(Film::getRate));

    @Override
    public void addLike(Long id, Long userId) {
        super.checkEntityId(id);
        super.checkEntityId(userId);
        super.data.get(id).addLike(userId);
    }

    @Override
    public void removeLike(Long id, Long userId) {
        super.checkEntityId(id);
        super.checkEntityId(userId);
        super.data.get(id).removeLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        sortedFilms.addAll(super.getAll());
        log.info(String.valueOf(sortedFilms));
        return sortedFilms.stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}