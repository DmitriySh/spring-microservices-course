package ru.shishmakov.dao;

import java.util.Collection;

public interface Dao<T> {

    default void save(T genre) {
        throw new UnsupportedOperationException();
    }

    default void update(T genre) {
        throw new UnsupportedOperationException();
    }

    default void delete(T genre) {
        throw new UnsupportedOperationException();
    }

    default T getById(long id) {
        throw new UnsupportedOperationException();
    }

    default Collection<T> getAll() {
        throw new UnsupportedOperationException();
    }
}
