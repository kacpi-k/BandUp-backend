package dev.kkoncki.bandup.user.management.instrument;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.instrument.forms.CreateInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.repository.InstrumentRepository;
import dev.kkoncki.bandup.user.management.instrument.service.InstrumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstrumentServiceTest {

    @Mock
    private InstrumentRepository instrumentRepository;

    @InjectMocks
    private InstrumentServiceImpl instrumentService;

    private Instrument instrument;
    private CreateInstrumentForm createForm;

    @BeforeEach
    void setUp() {
        instrument = Instrument.builder()
                .id("instrument-id")
                .name("Guitar")
                .categoryId("category-id")
                .build();

        createForm = new CreateInstrumentForm("Guitar", "category-id");
    }

    @Test
    void shouldSaveInstrument() {
        when(instrumentRepository.save(any(Instrument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Instrument result = instrumentService.save(createForm);

        assertNotNull(result);
        assertEquals("Guitar", result.getName());
        assertEquals("category-id", result.getCategoryId());
        verify(instrumentRepository).save(any(Instrument.class));
    }

    @Test
    void shouldGetInstrumentById() {
        when(instrumentRepository.findById("instrument-id")).thenReturn(Optional.of(instrument));

        Instrument result = instrumentService.get("instrument-id");

        assertNotNull(result);
        assertEquals("instrument-id", result.getId());
        assertEquals("Guitar", result.getName());
        verify(instrumentRepository).findById("instrument-id");
    }

    @Test
    void shouldThrowExceptionWhenInstrumentNotFound() {
        when(instrumentRepository.findById("instrument-id")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                instrumentService.get("instrument-id")
        );

        assertEquals(ErrorCode.INSTRUMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldGetAllInstruments() {
        List<Instrument> instruments = List.of(instrument);
        when(instrumentRepository.findAll()).thenReturn(instruments);

        List<Instrument> result = instrumentService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Guitar", result.get(0).getName());
        verify(instrumentRepository).findAll();
    }

    @Test
    void shouldGetAllInstrumentsByCategoryId() {
        List<Instrument> instruments = List.of(instrument);
        when(instrumentRepository.findAllByCategoryId("category-id")).thenReturn(instruments);

        List<Instrument> result = instrumentService.getAllByCategoryId("category-id");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("category-id", result.get(0).getCategoryId());
        verify(instrumentRepository).findAllByCategoryId("category-id");
    }
}

