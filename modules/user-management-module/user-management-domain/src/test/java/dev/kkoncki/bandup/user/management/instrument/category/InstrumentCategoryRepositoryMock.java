package dev.kkoncki.bandup.user.management.instrument.category;

import dev.kkoncki.bandup.user.management.instrument.category.repository.InstrumentCategoryRepository;

import java.util.*;

public class InstrumentCategoryRepositoryMock implements InstrumentCategoryRepository {

    Map<String, InstrumentCategory> mockDB = new HashMap<>();

    @Override
    public Optional<InstrumentCategory> findById(String id) {
        return Optional.ofNullable(mockDB.get(id));
    }

    @Override
    public InstrumentCategory save(InstrumentCategory instrumentCategory) {
        mockDB.put(instrumentCategory.getId(), instrumentCategory);
        return instrumentCategory;
    }

    @Override
    public List<InstrumentCategory> findAll() {
        return new ArrayList<>(mockDB.values());
    }
}
