package dev.kkoncki.bandup.commons.genre.repository;

import dev.kkoncki.bandup.commons.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGenreRepository extends JpaRepository<GenreEntity, String> {
}
