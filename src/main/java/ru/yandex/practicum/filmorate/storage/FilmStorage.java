package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends Storage<Film>{
void addLike();
void deleteLike();

    List<Film> getTenPopularFilms();
}
