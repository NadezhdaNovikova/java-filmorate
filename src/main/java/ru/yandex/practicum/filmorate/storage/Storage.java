package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.Collection;

public interface Storage<T extends BaseEntity> {
    T getById(long id);
    Collection<T> getAll();
    void add(T entity);
    void change(T entity);
    void delete(T entity);
}
