package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage{
    void addLike(Like like);                         //Добавить фильму лак пользователя
    void removeLike(Like like);                      //Удалить у фильма лайк пользователя
    List<Long> getPopularFilms(Integer count);       //Список самых популярных фильмов, по умолчанию 10
}