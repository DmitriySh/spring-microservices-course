package ru.shishmakov.dao;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IRepository<T> {

    default void save(T genre) {
        throw new UnsupportedOperationException();
    }

    default void update(T genre) {
        throw new UnsupportedOperationException();
    }

    default void delete(long id) {
        throw new UnsupportedOperationException();
    }

    default Optional<T> getById(long id, Map<String, Object> context) {
        throw new UnsupportedOperationException();
    }

    default Collection<T> getAll() {
        throw new UnsupportedOperationException();
    }

    default Collection<T> getAll(Set<Long> ids) {
        throw new UnsupportedOperationException();
    }
}
