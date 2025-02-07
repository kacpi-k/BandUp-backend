package dev.kkoncki.bandup.user.management.instrument.category;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.instrument.category.forms.CreateInstrumentCategoryForm;
import dev.kkoncki.bandup.user.management.instrument.category.repository.InstrumentCategoryRepository;
import dev.kkoncki.bandup.user.management.instrument.category.service.InstrumentCategoryServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstrumentCategoryServiceTest {

    @Mock
    private InstrumentCategoryRepository instrumentCategoryRepository;

    @InjectMocks
    private InstrumentCategoryServiceImpl instrumentCategoryService;

    private CreateInstrumentCategoryForm createInstrumentCategoryForm;
    private InstrumentCategory instrumentCategory;

    @BeforeEach
    void setUp() {
        createInstrumentCategoryForm = new CreateInstrumentCategoryForm("Percussion");

        instrumentCategory = InstrumentCategory.builder()
                .id("category-123")
                .name("Percussion")
                .build();
    }

    @Test
    void shouldSaveInstrumentCategorySuccessfully() {
        when(instrumentCategoryRepository.save(any(InstrumentCategory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        InstrumentCategory savedCategory = instrumentCategoryService.save(createInstrumentCategoryForm);

        assertNotNull(savedCategory);
        assertEquals("Percussion", savedCategory.getName());

        verify(instrumentCategoryRepository, times(1)).save(any(InstrumentCategory.class));
    }

    @Test
    void shouldGetInstrumentCategorySuccessfully() {
        when(instrumentCategoryRepository.findById("category-123")).thenReturn(Optional.of(instrumentCategory));

        InstrumentCategory fetchedCategory = instrumentCategoryService.get("category-123");

        assertNotNull(fetchedCategory);
        assertEquals("category-123", fetchedCategory.getId());
        assertEquals("Percussion", fetchedCategory.getName());

        verify(instrumentCategoryRepository, times(1)).findById("category-123");
    }

    @Test
    void shouldThrowExceptionWhenInstrumentCategoryNotFound() {
        when(instrumentCategoryRepository.findById("invalid-id")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> instrumentCategoryService.get("invalid-id"));

        assertEquals(ErrorCode.INSTRUMENT_CATEGORY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldReturnAllInstrumentCategories() {
        List<InstrumentCategory> categories = List.of(
                new InstrumentCategory("category-123", "Percussion"),
                new InstrumentCategory("category-456", "Strings"),
                new InstrumentCategory("category-789", "Woodwind")
        );

        when(instrumentCategoryRepository.findAll()).thenReturn(categories);

        List<InstrumentCategory> result = instrumentCategoryService.getAll();

        assertEquals(3, result.size());
        assertEquals("Percussion", result.get(0).getName());
        assertEquals("Strings", result.get(1).getName());
        assertEquals("Woodwind", result.get(2).getName());

        verify(instrumentCategoryRepository, times(1)).findAll();
    }
}

