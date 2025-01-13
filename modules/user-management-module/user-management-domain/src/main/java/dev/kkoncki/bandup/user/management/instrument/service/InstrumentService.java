package dev.kkoncki.bandup.user.management.instrument.service;

import dev.kkoncki.bandup.user.management.instrument.Instrument;
import dev.kkoncki.bandup.user.management.instrument.forms.CreateInstrumentForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface InstrumentService {
    Instrument get(String instrumentId);
    Instrument save(@Valid CreateInstrumentForm form);
    List<Instrument> getAll();
    List<Instrument> getAllByCategoryId(String categoryId);
}
