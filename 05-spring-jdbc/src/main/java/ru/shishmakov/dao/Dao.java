package ru.shishmakov.dao;

import java.util.List;

public interface Dao<T> {

    void save(T genre);

    void update(T genre);

    void delete(T genre);

    T getById(int id);

    List<T> getAll();
}
