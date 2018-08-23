package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Repository
public class AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class)
                .getResultList();
    }

    public List<Author> getByIds(Set<Long> ids) {
        return em.createQuery("select a from Author a where a.id in (:ids)", Author.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
