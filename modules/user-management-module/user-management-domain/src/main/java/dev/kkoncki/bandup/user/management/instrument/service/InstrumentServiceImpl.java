package dev.kkoncki.bandup.user.management.instrument.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.instrument.Instrument;
import dev.kkoncki.bandup.user.management.instrument.forms.CreateInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.repository.InstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;

    public InstrumentServiceImpl(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    private Instrument getByIdOrThrowInstrument(String instrumentId) {
        return instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INSTRUMENT_NOT_FOUND));
    }

    @Override
    public Instrument get(String instrumentId) {
        return getByIdOrThrowInstrument(instrumentId);
    }

    @Override
    public Instrument save(CreateInstrumentForm form) {
        Instrument instrument = Instrument.builder()
                .name(form.getName())
                .categoryId(form.getCategoryId())
                .build();
        return instrumentRepository.save(instrument);
    }

    @Override
    public List<Instrument> getAll() {
        return instrumentRepository.findAll();
    }

    @Override
    public List<Instrument> getAllByCategoryId(String categoryId) {
        return instrumentRepository.findAllByCategoryId(categoryId);
    }
}
