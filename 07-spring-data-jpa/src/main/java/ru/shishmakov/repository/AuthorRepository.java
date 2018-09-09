package ru.shishmakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

}
