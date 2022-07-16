package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface Storage<T extends BaseEntity> {

    Optional<T> getById(long id);      //Получить объект по id
    List<T> getAll();                  //Получить все объекты
    T add(T entity);                   //Добавить объект
    Optional<Film> change(T entity);             //Изменить объект
    void delete(T entity);             //Удалить объект
}