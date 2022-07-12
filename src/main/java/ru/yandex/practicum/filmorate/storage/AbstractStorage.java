package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.BaseEntity;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AbstractStorage<T extends BaseEntity> implements Storage<T> {

    final HashMap<Long, T> data = new HashMap<>();
    private final IdGenerator idGenerator;

    @Autowired
    public AbstractStorage(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Optional<T> getById(long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<T> getAll() {
        return List.copyOf(data.values());
    }

    @Override
    public long add(T entity) {
        entity.setId(idGenerator.getId());
        data.put(entity.getId(), entity);
    return entity.getId();
    }

    @Override
    public void change(T entity) {
        data.put(entity.getId(), entity);
    }

    @Override
    public void delete(T entity) {
        data.remove(entity.getId());
    }
}