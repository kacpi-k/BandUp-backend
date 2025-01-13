package dev.kkoncki.bandup.user.management.instrument.repository;

import dev.kkoncki.bandup.user.management.instrument.Instrument;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository {
    Optional<Instrument> findById(String id);
    Instrument save(Instrument instrument);
    List<Instrument> findAll();
    List<Instrument> findAllByCategoryId(String categoryId);
}
