package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends Storage<Film> {

    void addLike(Long id, Long userId);         //Добавить фильму лак пользователя
    void removeLike(Long id, Long userId);      //Удалить у фильма лайк пользователя
    List<Film> getPopularFilms(Integer count);  //Список самых популярных фильмов, по умолчанию 10

}