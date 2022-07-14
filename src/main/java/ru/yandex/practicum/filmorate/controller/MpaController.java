package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.MpaDBStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaDBStorage mpaStorage;

    @Autowired
    public MpaController(MpaDBStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping("/mpa")
    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    @GetMapping("/mpa/{id}")
    public Optional<Mpa> getById(@PathVariable("id") final Integer id) throws EntityNotFoundException {
        return Optional.ofNullable(mpaStorage.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Рейтинг с id = %s не найден", id))));
    }
}