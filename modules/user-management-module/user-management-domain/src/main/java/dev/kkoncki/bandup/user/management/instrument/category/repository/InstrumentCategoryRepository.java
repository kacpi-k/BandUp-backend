package dev.kkoncki.bandup.user.management.instrument.category.repository;

import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategory;

import java.util.List;
import java.util.Optional;

public interface InstrumentCategoryRepository {
    Optional<InstrumentCategory> findById(String id);
    InstrumentCategory save(InstrumentCategory instrumentCategory);
    List<InstrumentCategory> findAll();
}
