package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage extends Storage<Film> {

    @Override
    Optional<Film> getById(long id);

    @Override
    List<Film> getAll();

    @Override
    Optional<Film> add(Film film);

    @Override
    Optional<Film> change(Film film);

    @Override
    void delete(Film film);

    void addLike(Long id, Long userId);         //Добавить фильму лак пользователя
    void removeLike(Long id, Long userId);      //Удалить у фильма лайк пользователя
    List<Film> getPopularFilms(Integer count);  //Список самых популярных фильмов, по умолчанию 10

}