package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {
    @Override
    public void add(){
//TODO
    }

    @Override
    public void change() {
//TODO
    }

    @Override
    public void delete() {
//TODO
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
