package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController

public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaStorage) {
        this.mpaService = mpaStorage;
    }

    @RequestMapping("/mpa")

    @GetMapping("/mpa")
    public List<Mpa> getAll() {
        return mpaService.getAll();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getById(@PathVariable("id") Integer id){
        return mpaService.getById(id);
    }
}