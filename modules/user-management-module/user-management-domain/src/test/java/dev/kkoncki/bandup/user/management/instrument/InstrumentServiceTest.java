package dev.kkoncki.bandup.user.management.instrument;

import dev.kkoncki.bandup.user.management.instrument.forms.CreateInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.repository.InstrumentRepository;
import dev.kkoncki.bandup.user.management.instrument.service.InstrumentService;
import dev.kkoncki.bandup.user.management.instrument.service.InstrumentServiceImpl;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InstrumentServiceTest {

    InstrumentRepository repository = new InstrumentRepositoryMock();

    InstrumentService service = new InstrumentServiceImpl(repository);

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
        CreateInstrumentForm form = new CreateInstrumentForm("Guitar", "1");
        Instrument instrument = service.save(form);

        Instrument instrumentFound = service.get(instrument.getId());

        assertEquals(instrument.getId(), instrumentFound.getId());
        assertEquals(instrument.getName(), instrumentFound.getName());
        assertEquals(instrument.getCategoryId(), instrumentFound.getCategoryId());
    }

    @Test
    void saveTest() {
        CreateInstrumentForm form = new CreateInstrumentForm("Guitar", "1");
        Instrument instrument = service.save(form);

        assertEquals("Guitar", instrument.getName());
        assertEquals("1", instrument.getCategoryId());
    }

    @Test
    void getAllTest() {
        CreateInstrumentForm form1 = new CreateInstrumentForm("Guitar", "1");
        CreateInstrumentForm form2 = new CreateInstrumentForm("Drums", "2");

        Instrument instrument1 = service.save(form1);
        Instrument instrument2 = service.save(form2);

        List<Instrument> instruments = service.getAll();

        assertTrue(instruments.contains(instrument1));
        assertTrue(instruments.contains(instrument2));
        assertEquals(2, instruments.size());
    }

    @Test
    void getAllByCategoryIdTest() {
        CreateInstrumentForm form1 = new CreateInstrumentForm("Guitar", "1");
        CreateInstrumentForm form2 = new CreateInstrumentForm("Drums", "2");
        CreateInstrumentForm form3 = new CreateInstrumentForm("Bass", "1");

        Instrument instrument1 = service.save(form1);
        Instrument instrument2 = service.save(form2);
        Instrument instrument3 = service.save(form3);

        List<Instrument> instruments = service.getAllByCategoryId("1");

        assertTrue(instruments.contains(instrument1));
        assertFalse(instruments.contains(instrument2));
        assertTrue(instruments.contains(instrument3));
        assertEquals(2, instruments.size());
    }

    @Test
    void validateCreateInstrumentForm() {
        CreateInstrumentForm form = new CreateInstrumentForm("g", "1");

        genericViolationSet(form);
    }
}
