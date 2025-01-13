package dev.kkoncki.bandup.user.management.instrument.category.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategory;
import dev.kkoncki.bandup.user.management.instrument.category.forms.CreateInstrumentCategoryForm;
import dev.kkoncki.bandup.user.management.instrument.category.repository.InstrumentCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentCategoryServiceImpl implements InstrumentCategoryService {

    private final InstrumentCategoryRepository instrumentCategoryRepository;

    public InstrumentCategoryServiceImpl(InstrumentCategoryRepository instrumentCategoryRepository) {
        this.instrumentCategoryRepository = instrumentCategoryRepository;
    }

    private InstrumentCategory getByIdOrThrowInstrumentCategory(String InstrumentCategoryId) {
        return instrumentCategoryRepository.findById(InstrumentCategoryId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INSTRUMENT_CATEGORY_NOT_FOUND));
    }

    @Override
    public InstrumentCategory get(String InstrumentCategoryId) {
        return getByIdOrThrowInstrumentCategory(InstrumentCategoryId);
    }

    @Override
    public InstrumentCategory save(CreateInstrumentCategoryForm form) {
        InstrumentCategory instrumentCategory = InstrumentCategory.builder()
                .name(form.getName())
                .build();
        return instrumentCategoryRepository.save(instrumentCategory);
    }

    @Override
    public List<InstrumentCategory> getAll() {
        return instrumentCategoryRepository.findAll();
    }

}
