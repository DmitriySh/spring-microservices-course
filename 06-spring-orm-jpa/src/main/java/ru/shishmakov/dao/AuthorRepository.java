package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Collection;
import java.util.Set;

@Repository
public class AuthorRepository {
    private EntityManager em;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory em) {
        this.em = em.createEntityManager();
    }

    public Collection<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class)
                .getResultList();
    }

    public Collection<Author> getByIds(Set<Long> ids) {
        return em.createQuery("select a from Author a where a.id in (:ids)", Author.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
