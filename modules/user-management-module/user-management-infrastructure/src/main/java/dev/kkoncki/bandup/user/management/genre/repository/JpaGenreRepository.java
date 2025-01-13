package dev.kkoncki.bandup.user.management.genre.repository;

import dev.kkoncki.bandup.user.management.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGenreRepository extends JpaRepository<GenreEntity, String> {
}
