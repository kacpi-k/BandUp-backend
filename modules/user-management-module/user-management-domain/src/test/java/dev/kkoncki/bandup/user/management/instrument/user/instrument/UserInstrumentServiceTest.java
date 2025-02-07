package dev.kkoncki.bandup.user.management.instrument.user.instrument;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.instrument.SkillLevel;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.CreateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.UpdateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.repository.UserInstrumentRepository;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.service.UserInstrumentServiceImpl;
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
class UserInstrumentServiceTest {

    @Mock
    private UserInstrumentRepository userInstrumentRepository;

    @InjectMocks
    private UserInstrumentServiceImpl userInstrumentService;

    private UserInstrument userInstrument;
    private CreateUserInstrumentForm createForm;
    private UpdateUserInstrumentForm updateForm;

    @BeforeEach
    void setUp() {
        userInstrument = UserInstrument.builder()
                .id("instrument-id")
                .userId("user-id")
                .instrumentId("guitar-id")
                .skillLevel(SkillLevel.ADVANCED)
                .build();

        createForm = new CreateUserInstrumentForm("guitar-id", SkillLevel.ADVANCED);
        updateForm = new UpdateUserInstrumentForm("instrument-id", "piano-id", SkillLevel.BEGINNER);
    }

    @Test
    void shouldSaveUserInstrument() {
        when(userInstrumentRepository.existsByUserIdAndInstrumentId("user-id", "guitar-id")).thenReturn(false);
        when(userInstrumentRepository.save(any(UserInstrument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserInstrument result = userInstrumentService.save(createForm, "user-id");

        assertNotNull(result);
        assertEquals("user-id", result.getUserId());
        assertEquals("guitar-id", result.getInstrumentId());
        assertEquals(SkillLevel.ADVANCED, result.getSkillLevel());

        verify(userInstrumentRepository).save(any(UserInstrument.class));
    }

    @Test
    void shouldThrowExceptionWhenUserInstrumentAlreadyExists() {
        when(userInstrumentRepository.existsByUserIdAndInstrumentId("user-id", "guitar-id")).thenReturn(true);

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                userInstrumentService.save(createForm, "user-id")
        );

        assertEquals(ErrorCode.USER_INSTRUMENT_ALREADY_EXISTS, exception.getErrorCode());
        verify(userInstrumentRepository, never()).save(any(UserInstrument.class));
    }

    @Test
    void shouldUpdateUserInstrument() {
        when(userInstrumentRepository.findById("instrument-id")).thenReturn(Optional.of(userInstrument));

        userInstrumentService.update(updateForm);

        assertEquals("piano-id", userInstrument.getInstrumentId());
        assertEquals(SkillLevel.BEGINNER, userInstrument.getSkillLevel());

        verify(userInstrumentRepository).save(userInstrument);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentUserInstrument() {
        when(userInstrumentRepository.findById("instrument-id")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                userInstrumentService.update(updateForm)
        );

        assertEquals(ErrorCode.USER_INSTRUMENT_NOT_FOUND, exception.getErrorCode());
        verify(userInstrumentRepository, never()).save(any(UserInstrument.class));
    }

    @Test
    void shouldGetUserInstrumentById() {
        when(userInstrumentRepository.findById("instrument-id")).thenReturn(Optional.of(userInstrument));

        UserInstrument result = userInstrumentService.get("instrument-id");

        assertNotNull(result);
        assertEquals("instrument-id", result.getId());
        assertEquals("guitar-id", result.getInstrumentId());
        verify(userInstrumentRepository).findById("instrument-id");
    }

    @Test
    void shouldThrowExceptionWhenGettingNonexistentUserInstrument() {
        when(userInstrumentRepository.findById("instrument-id")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                userInstrumentService.get("instrument-id")
        );

        assertEquals(ErrorCode.USER_INSTRUMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldGetAllUserInstrumentsByUserId() {
        List<UserInstrument> instruments = List.of(userInstrument);
        when(userInstrumentRepository.findAllByUserId("user-id")).thenReturn(instruments);

        List<UserInstrument> result = userInstrumentService.getAllByUserId("user-id");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("guitar-id", result.get(0).getInstrumentId());
        verify(userInstrumentRepository).findAllByUserId("user-id");
    }

    @Test
    void shouldDeleteUserInstrument() {
        userInstrumentService.delete("instrument-id");

        verify(userInstrumentRepository).delete("instrument-id");
    }
}
