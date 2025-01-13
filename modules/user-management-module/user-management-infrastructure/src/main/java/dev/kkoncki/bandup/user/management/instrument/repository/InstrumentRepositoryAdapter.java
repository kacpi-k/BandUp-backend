package dev.kkoncki.bandup.user.management.instrument.repository;

import dev.kkoncki.bandup.user.management.instrument.Instrument;
import dev.kkoncki.bandup.user.management.instrument.InstrumentEntity;
import dev.kkoncki.bandup.user.management.instrument.InstrumentMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InstrumentRepositoryAdapter implements InstrumentRepository{

    private final JpaInstrumentRepository jpaInstrumentRepository;

    public InstrumentRepositoryAdapter(JpaInstrumentRepository jpaInstrumentRepository) {
        this.jpaInstrumentRepository = jpaInstrumentRepository;
    }

    @Override
    public Optional<Instrument> findById(String id) {
        return jpaInstrumentRepository.findById(id).map(InstrumentMapper::toDomain);
    }

    @Override
    public Instrument save(Instrument instrument) {
        InstrumentEntity entity = InstrumentMapper.toEntity(instrument);
        return InstrumentMapper.toDomain(jpaInstrumentRepository.save(entity));
    }

    @Override
    public List<Instrument> findAll() {
        return jpaInstrumentRepository.findAll().stream()
                .map(InstrumentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Instrument> findAllByCategoryId(String categoryId) {
        return jpaInstrumentRepository.findAllByCategory_Id(categoryId).stream()
                .map(InstrumentMapper::toDomain)
                .toList();
    }
}
