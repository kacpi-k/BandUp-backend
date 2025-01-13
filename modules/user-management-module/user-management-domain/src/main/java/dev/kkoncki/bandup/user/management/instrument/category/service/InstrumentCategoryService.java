package dev.kkoncki.bandup.user.management.instrument.category.service;

import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategory;
import dev.kkoncki.bandup.user.management.instrument.category.forms.CreateInstrumentCategoryForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface InstrumentCategoryService {
    InstrumentCategory get(String id);
    InstrumentCategory save(@Valid CreateInstrumentCategoryForm form);
    List<InstrumentCategory> getAll();
}
