package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseEntity;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
public class AbstractStorage<T extends BaseEntity> implements Storage<T> {

    final HashMap<Long, T> data = new HashMap<>();
    @Autowired
    private IdGenerator idGenerator;

    @Override
    public T getById(long id) {
        checkEntityId(id);
        return data.get(id);
    }

    @Override
    public Collection<T> getAll() {
        return data.values();
    }

    @Override
    public void add(T entity) {
        entity.setId(idGenerator.getId());
        data.put(entity.getId(), entity);
    }

    @Override
    public void change(T entity) {
        checkEntityId(entity.getId());
        data.put(entity.getId(), entity);
    }

    @Override
    public void delete(T entity) {
        checkEntityId(entity.getId());
        data.remove(entity.getId());
    }

    void checkEntityId(Long id) {
        if (id < 1 | !data.containsKey(id)) {
            log.info("id: " + id);
            throw new EntityNotFoundException("Объект не найден");
        }
    }
}