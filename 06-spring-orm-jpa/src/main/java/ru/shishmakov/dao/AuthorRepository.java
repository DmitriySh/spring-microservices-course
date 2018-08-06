package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Repository
public class AuthorRepository implements IRepository<Author> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class)
                .getResultList();
    }
}
