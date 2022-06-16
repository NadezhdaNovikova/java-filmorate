package ru.yandex.practicum.filmorate.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IdGenerator {

    private long id = 0;

    public long getId() {
        return ++id;
    }
}