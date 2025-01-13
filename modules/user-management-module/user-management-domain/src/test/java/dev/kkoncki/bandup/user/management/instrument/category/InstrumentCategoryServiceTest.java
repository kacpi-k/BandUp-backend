package dev.kkoncki.bandup.user.management.instrument.category;

import dev.kkoncki.bandup.user.management.instrument.category.forms.CreateInstrumentCategoryForm;
import dev.kkoncki.bandup.user.management.instrument.category.repository.InstrumentCategoryRepository;
import dev.kkoncki.bandup.user.management.instrument.category.service.InstrumentCategoryService;
import dev.kkoncki.bandup.user.management.instrument.category.service.InstrumentCategoryServiceImpl;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InstrumentCategoryServiceTest {

    InstrumentCategoryRepository repository = new InstrumentCategoryRepositoryMock();

    InstrumentCategoryService service = new InstrumentCategoryServiceImpl(repository);

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private <T> void genericViolationSet(T form) {
        Set<ConstraintViolation<T>> violations = validator.validate(form);

        assertThrows(ConstraintViolationException.class, () -> {
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException("Validation failed", violations);
            }
        });
    }

    @Test
    void getTest() {
        CreateInstrumentCategoryForm form = new CreateInstrumentCategoryForm("Strings");
        InstrumentCategory category = service.save(form);

        InstrumentCategory categoryFound = service.get(category.getId());

        assertEquals(category.getId(), categoryFound.getId());
        assertEquals(category.getName(), categoryFound.getName());
    }

    @Test
    void saveTest() {
        CreateInstrumentCategoryForm form = new CreateInstrumentCategoryForm("Strings");
        InstrumentCategory category = service.save(form);

        assertNotNull(category.getId());
        assertEquals("Strings", category.getName());
    }

    @Test
    void getAllTest() {
        CreateInstrumentCategoryForm form1 = new CreateInstrumentCategoryForm("Strings");
        CreateInstrumentCategoryForm form2 = new CreateInstrumentCategoryForm("Percussion");

        InstrumentCategory category1 = service.save(form1);
        InstrumentCategory category2 = service.save(form2);

        List<InstrumentCategory> categories = service.getAll();

        assertTrue(categories.contains(category1));
        assertTrue(categories.contains(category2));
        assertEquals(2, categories.size());
    }

    @Test
    void validateInstrumentCategoryForm() {
        CreateInstrumentCategoryForm form = new CreateInstrumentCategoryForm("T");

        genericViolationSet(form);
    }
}
