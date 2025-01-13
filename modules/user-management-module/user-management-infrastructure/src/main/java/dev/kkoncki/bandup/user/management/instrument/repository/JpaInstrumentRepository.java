package dev.kkoncki.bandup.user.management.instrument.repository;

import dev.kkoncki.bandup.user.management.instrument.InstrumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaInstrumentRepository extends JpaRepository<InstrumentEntity, String> {
    List<InstrumentEntity> findAllByCategory_Id(String categoryId);
}
