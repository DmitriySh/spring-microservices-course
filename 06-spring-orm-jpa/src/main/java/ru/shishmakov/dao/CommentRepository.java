package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.Set;

@Repository
public class CommentRepository {
    private EntityManager em;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory em) {
        this.em = em.createEntityManager();
    }

    public List<Comment> getAll() {
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    public List<Comment> getByIds(Set<Long> ids) {
        return em.createQuery("select c from Comment c where c.id in (:ids)", Comment.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public void save(Book book, Comment comment) {
        em.getTransaction().begin();
        try {
            book.addComment(comment);
            em.merge(book);
//            book.addComment(comment);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}
