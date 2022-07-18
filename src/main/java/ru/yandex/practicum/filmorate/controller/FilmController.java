package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private static final LocalDate VALID_DATE_FILM = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @RequestMapping("/films")

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping(value = "/films")
    public Optional<Film>  createFilm(@Valid @RequestBody Film film) {
        filmDateValidate(film);
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Optional<Film> updateFilm(@Valid @RequestBody Film film) {
        filmDateValidate(film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Optional<Film> getById(@PathVariable("id") Long id) {
        return filmService.getById(id);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long id,
                        @PathVariable("userId") Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Long id,
                           @PathVariable("userId") Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Optional<Film>> getPopularFilms(@RequestParam(defaultValue = "10", required = false)
                                          @Positive(message = "Число популярных фильмов должно быть больше 0.")
                                          Integer count) {
        return filmService.getPopularFilms(count);
    }


    private void filmDateValidate(Film film) {
        if (film.getReleaseDate().isBefore(VALID_DATE_FILM)) {
            log.info("Дата релиза: " + film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}