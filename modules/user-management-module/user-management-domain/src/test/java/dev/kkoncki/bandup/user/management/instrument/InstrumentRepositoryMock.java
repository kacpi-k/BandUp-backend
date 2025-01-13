package dev.kkoncki.bandup.user.management.instrument;

import dev.kkoncki.bandup.user.management.instrument.repository.InstrumentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InstrumentRepositoryMock implements InstrumentRepository {

    Map<String, Instrument> mockDB = new HashMap<>();

    @Override
    public Optional<Instrument> findById(String id) {
        return Optional.ofNullable(mockDB.get(id));
    }

    @Override
    public Instrument save(Instrument instrument) {
        mockDB.put(instrument.getId(), instrument);
        return instrument;
    }

    @Override
    public List<Instrument> findAll() {
        return new ArrayList<>(mockDB.values());
    }

    @Override
    public List<Instrument> findAllByCategoryId(String categoryId) {
        return mockDB.values().stream()
                .filter(instrument -> instrument.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }
}
