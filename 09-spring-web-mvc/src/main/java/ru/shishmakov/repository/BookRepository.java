package ru.shishmakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
