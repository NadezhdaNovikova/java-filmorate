package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.List;

public interface Storage<T extends BaseEntity> {
    T getById(long id);
    List<T> getAll();
    void add();
    void change();
    void delete();
}
