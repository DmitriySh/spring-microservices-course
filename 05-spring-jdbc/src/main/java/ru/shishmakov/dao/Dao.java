package ru.shishmakov.dao;

import java.util.Collection;

public interface Dao<T> {

    void save(T genre);

    void update(T genre);

    void delete(T genre);

    T getById(int id);

    Collection<T> getAll();
}
