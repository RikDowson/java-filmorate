package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getMpa() {
        return mpaStorage.getAll();
    }

    public Mpa getMpaById(Integer id) {
        return mpaStorage.findById(id).orElseThrow(
                () -> new NotFoundException("MPA с id " + id + " не найден!")
        );
    }
}