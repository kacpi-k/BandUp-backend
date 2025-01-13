package dev.kkoncki.bandup.user.management.instrument.category.repository;

import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategory;
import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategoryEntity;
import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InstrumentCategoryRepositoryAdapter implements InstrumentCategoryRepository{

    private final JpaInstrumentCategoryRepository jpaInstrumentCategoryRepository;

    public InstrumentCategoryRepositoryAdapter(JpaInstrumentCategoryRepository jpaInstrumentCategoryRepository) {
        this.jpaInstrumentCategoryRepository = jpaInstrumentCategoryRepository;
    }

    @Override
    public Optional<InstrumentCategory> findById(String id) {
        return jpaInstrumentCategoryRepository.findById(id).map(InstrumentCategoryMapper::toDomain);
    }

    @Override
    public InstrumentCategory save(InstrumentCategory instrumentCategory) {
        InstrumentCategoryEntity entity = InstrumentCategoryMapper.toEntity(instrumentCategory);
        return InstrumentCategoryMapper.toDomain(jpaInstrumentCategoryRepository.save(entity));
    }

    @Override
    public List<InstrumentCategory> findAll() {
        return jpaInstrumentCategoryRepository.findAll().stream()
                .map(InstrumentCategoryMapper::toDomain)
                .toList();
    }
}
