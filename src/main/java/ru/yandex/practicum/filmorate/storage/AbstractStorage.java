package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseEntity;

import java.util.HashMap;
import java.util.List;

public class AbstractStorage<T extends BaseEntity> implements Storage<T>{
    private final HashMap<Long, BaseEntity> data = new HashMap<>();
    private long idGenerator;

    @Override
    public T getById(long id) {
        return null;
    }

    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public void add() {

    }

    @Override
    public void change() {

    }

    @Override
    public void delete() {

    }
}
