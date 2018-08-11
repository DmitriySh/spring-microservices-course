package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.Set;

@Repository
public class GenreRepository {
    private EntityManager em;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory em) {
        this.em = em.createEntityManager();
    }

    public List<Genre> getAll() {
        return em.createQuery("select g from Genre g", Genre.class)
                .getResultList();
    }

    public List<Genre> getByIds(Set<Long> ids) {
        return em.createQuery("select g from Genre g where g.id in (:ids)", Genre.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
