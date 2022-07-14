package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface Storage<T extends BaseEntity> {

    Optional<T> getById(long id);      //Получить объект по id
    List<T> getAll();                  //Получить все объекты
    T add(T entity);                   //Добавить объект
    void change(T entity);             //Изменить объект
    void delete(T entity);             //Удалить объект
}