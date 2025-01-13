package dev.kkoncki.bandup.user.management.instrument.category.repository;

import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInstrumentCategoryRepository extends JpaRepository<InstrumentCategoryEntity, String> {
}
