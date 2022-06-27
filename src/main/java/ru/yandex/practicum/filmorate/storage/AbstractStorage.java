package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.BaseEntity;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
public class AbstractStorage<T extends BaseEntity> implements Storage<T>{

    private final HashMap<Long, T> data = new HashMap<>();
    @Autowired
    private IdGenerator idGenerator;

    @Override
    public T getById(long id) {
        return data.get(id);
    }

    @Override
    public Collection<T> getAll() {
        return data.values();
    }

    @Override
    public void add(T entity) {
        log.info("Фильм  " + entity);
        entity.setId(idGenerator.getId());
        log.info("Id:    " + entity.getId());
        data.put(entity.getId(), entity);
    }

    @Override
    public void change(T entity) {
        if (entity.getId() < 1 | !data.containsKey(entity.getId())) {
            log.info("id: " + entity.getId());
            throw new ValidationException("Обновление невозможно. Некорректный id");
        }
        data.put(entity.getId(), entity);
    }

    @Override
    public void delete(T entity) {
data.remove(entity.getId());
    }
}
