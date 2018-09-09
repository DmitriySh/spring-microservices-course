package ru.shishmakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

}
