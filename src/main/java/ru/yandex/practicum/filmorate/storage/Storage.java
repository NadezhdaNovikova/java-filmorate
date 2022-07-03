package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.Collection;
import java.util.Optional;

public interface Storage<T extends BaseEntity> {
    Optional<T> getById(long id);      //Получить объект по id
    Collection<T> getAll();  //Получить все объекты
    void add(T entity);      //Добавить объект
    void change(T entity);   //Изменить объект
    void delete(T entity);   //Удалить объект
}