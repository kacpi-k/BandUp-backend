package dev.kkoncki.bandup.user.management;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.commons.search.*;
import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import dev.kkoncki.bandup.user.management.forms.UpdateUserLocationForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.service.UserInstrumentService;
import dev.kkoncki.bandup.user.management.repository.UserManagementRepository;
import dev.kkoncki.bandup.user.management.service.UserManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceTest {

    @Mock
    private UserManagementRepository userManagementRepository;

    @Mock
    private UserInstrumentService userInstrumentService;

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    private CreateUserForm createUserForm;
    private User user;

    @BeforeEach
    void setUp() {
        createUserForm = new CreateUserForm("John", "Doe", "john.doe@example.com");

        user = User.builder()
                .id("user-123")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdOn(Instant.now())
                .isBlocked(false)
                .instruments(new ArrayList<>())
                .bio(null)
                .genres(new ArrayList<>())
                .imageUrl(null)
                .latitude(null)
                .longitude(null)
                .city(null)
                .country(null)
                .build();
    }

    @Test
    void shouldSaveUserSuccessfully() {
        when(userManagementRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userManagementService.save(createUserForm);

        assertNotNull(savedUser);
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("john.doe@example.com", savedUser.getEmail());

        verify(userManagementRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldGetUserSuccessfully() {
        when(userManagementRepository.findById("user-123")).thenReturn(Optional.of(user));

        User fetchedUser = userManagementService.get("user-123");

        assertNotNull(fetchedUser);
        assertEquals("user-123", fetchedUser.getId());

        verify(userManagementRepository, times(1)).findById("user-123");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userManagementRepository.findById("invalid-id")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userManagementService.get("invalid-id"));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldBlockUserSuccessfully() {
        when(userManagementRepository.findById("user-123")).thenReturn(Optional.of(user));
        when(userManagementRepository.save(any(User.class))).thenReturn(user);

        User blockedUser = userManagementService.block("user-123");

        assertTrue(blockedUser.isBlocked());
        verify(userManagementRepository, times(1)).save(user);
    }

    @Test
    void shouldUnblockUserSuccessfully() {
        user.setBlocked(true);

        when(userManagementRepository.findById("user-123")).thenReturn(Optional.of(user));
        when(userManagementRepository.save(any(User.class))).thenReturn(user);

        User unblockedUser = userManagementService.unBlock("user-123");

        assertFalse(unblockedUser.isBlocked());
        verify(userManagementRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateUserLocationSuccessfully() {
        UpdateUserLocationForm locationForm = new UpdateUserLocationForm(52.2298, 21.0122, "Warsaw", "Poland");

        when(userManagementRepository.findById("user-123")).thenReturn(Optional.of(user));

        userManagementService.updateUserLocation("user-123", locationForm);

        assertEquals(52.2298, user.getLatitude());
        assertEquals(21.0122, user.getLongitude());
        assertEquals("Warsaw", user.getCity());
        assertEquals("Poland", user.getCountry());

        verify(userManagementRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateUserBioSuccessfully() {
        when(userManagementRepository.findById("user-123")).thenReturn(Optional.of(user));

        userManagementService.updateBio("user-123", "New bio");

        assertEquals("New bio", user.getBio());
        verify(userManagementRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateUserImageUrlSuccessfully() {
        when(userManagementRepository.findById("user-123")).thenReturn(Optional.of(user));

        userManagementService.updateImageUrl("user-123", "https://example.com/image.jpg");

        assertEquals("https://example.com/image.jpg", user.getImageUrl());
        verify(userManagementRepository, times(1)).save(user);
    }

    @Test
    void shouldAddUserInstrument() {
        when(userManagementRepository.findById("user-id")).thenReturn(Optional.of(user));

        userManagementService.addUserInstrument("instrument-id", "user-id");

        assertTrue(user.getInstruments().contains("instrument-id"));

        verify(userManagementRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyHasInstrument() {
        user.getInstruments().add("instrument-id");

        when(userManagementRepository.findById("user-id")).thenReturn(Optional.of(user));

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                userManagementService.addUserInstrument("instrument-id", "user-id")
        );

        assertEquals(ErrorCode.USER_ALREADY_HAS_INSTRUMENT, exception.getErrorCode());
        verify(userManagementRepository, never()).save(any(User.class));
    }

    @Test
    void shouldRemoveUserInstrument() {
        user.getInstruments().add("instrument-id");

        when(userManagementRepository.findById("user-id")).thenReturn(Optional.of(user));

        userManagementService.removeUserInstrument("instrument-id", "user-id");

        assertFalse(user.getInstruments().contains("instrument-id"));

        verify(userManagementRepository).save(user);

        verify(userInstrumentService).delete("instrument-id");
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotHaveInstrument() {
        when(userManagementRepository.findById("user-id")).thenReturn(Optional.of(user));

        ApplicationException exception = assertThrows(ApplicationException.class, () ->
                userManagementService.removeUserInstrument("instrument-id", "user-id")
        );

        assertEquals(ErrorCode.USER_DOES_NOT_HAVE_INSTRUMENT, exception.getErrorCode());
        verify(userManagementRepository, never()).save(any(User.class));
        verify(userInstrumentService, never()).delete(anyString());
    }

    @Test
    void shouldAddGenreSuccessfully() {
        when(userManagementRepository.findById("user-123")).thenReturn(Optional.of(user));

        userManagementService.addOrRemoveGenre("rock", "user-123");

        assertTrue(user.getGenres().contains("rock"));
    }

    @Test
    void shouldRemoveGenreIfAlreadyExists() {
        user.getGenres().add("rock");

        when(userManagementRepository.findById("user-123")).thenReturn(Optional.of(user));

        userManagementService.addOrRemoveGenre("rock", "user-123");

        assertFalse(user.getGenres().contains("rock"));
    }

    @Test
    void shouldSearchUsersByCriteria() {
        SearchForm form = new SearchForm(
                List.of(new SearchFormCriteria("firstName", "John", CriteriaOperator.EQUALS)),
                1, 10, new SearchSort("firstName", SearchSortOrder.ASC)
        );

        List<User> users = List.of(
                new User("id1", "John", "Doe", "john.doe@example.com", Instant.now(), false, new ArrayList<>(), "Bio", new ArrayList<>(), null, null, null, null, null)
        );

        SearchResponse<User> expectedResponse = new SearchResponse<>(users, 1L);

        when(userManagementRepository.search(form)).thenReturn(expectedResponse);

        SearchResponse<User> actualResponse = userManagementService.search(form);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getItems().size());
        assertEquals("John", actualResponse.getItems().get(0).getFirstName());
        verify(userManagementRepository, times(1)).search(form);
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersMatchCriteria() {
        SearchForm form = new SearchForm(
                List.of(new SearchFormCriteria("firstName", "NonExistingName", CriteriaOperator.EQUALS)),
                1, 10, new SearchSort("firstName", SearchSortOrder.ASC)
        );

        SearchResponse<User> expectedResponse = new SearchResponse<>(Collections.emptyList(), 0L);

        when(userManagementRepository.search(form)).thenReturn(expectedResponse);

        SearchResponse<User> actualResponse = userManagementService.search(form);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.getItems().size());
        assertEquals(0L, actualResponse.getTotal());
        verify(userManagementRepository, times(1)).search(form);
    }

    @Test
    void shouldSearchUsersWithLikeOperator() {
        SearchForm form = new SearchForm(
                List.of(new SearchFormCriteria("firstName", "Jo", CriteriaOperator.LIKE)),
                1, 10, new SearchSort("firstName", SearchSortOrder.ASC)
        );

        List<User> users = List.of(
                new User("id1", "John", "Doe", "john.doe@example.com", Instant.now(), false, new ArrayList<>(), "Bio", new ArrayList<>(), null, null, null, null, null),
                new User("id2", "Johnny", "Bravo", "johnny@example.com", Instant.now(), false, new ArrayList<>(), "Bio", new ArrayList<>(), null, null, null, null, null)
        );

        SearchResponse<User> expectedResponse = new SearchResponse<>(users, 2L);

        when(userManagementRepository.search(form)).thenReturn(expectedResponse);

        SearchResponse<User> actualResponse = userManagementService.search(form);

        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.getItems().size());
        verify(userManagementRepository, times(1)).search(form);
    }

    @Test
    void shouldSearchUsersWithGreaterThanOperator() {
        SearchForm form = new SearchForm(
                List.of(new SearchFormCriteria("createdOn", Instant.now().minusSeconds(3600).toString(), CriteriaOperator.GR)),
                1, 10, new SearchSort("createdOn", SearchSortOrder.ASC)
        );

        List<User> users = List.of(
                new User("id1", "John", "Doe", "john.doe@example.com", Instant.now(), false, new ArrayList<>(), "Bio", new ArrayList<>(), null, null, null, null, null)
        );

        SearchResponse<User> expectedResponse = new SearchResponse<>(users, 1L);

        when(userManagementRepository.search(form)).thenReturn(expectedResponse);

        SearchResponse<User> actualResponse = userManagementService.search(form);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.getItems().size());
        verify(userManagementRepository, times(1)).search(form);
    }

    @Test
    void shouldSearchUsersWithinRadius() {
        SearchForm form = new SearchForm(
                List.of(new SearchFormCriteria("location", Map.of(
                        "latitude", 52.2298,
                        "longitude", 21.0122,
                        "radiusKm", 50.0
                ), CriteriaOperator.DISTANCE)),
                1, 10, new SearchSort("createdOn", SearchSortOrder.ASC)
        );

        List<User> users = List.of(
                new User("id1", "John", "Doe", "john.doe@example.com", Instant.now(), false, new ArrayList<>(), "Bio", new ArrayList<>(), null, 52.23, 21.01, "Warsaw", "Poland"),
                new User("id2", "Jane", "Smith", "jane.smith@example.com", Instant.now(), false, new ArrayList<>(), "Bio", new ArrayList<>(), null, 52.40, 21.20, "Warsaw", "Poland")
        );

        SearchResponse<User> expectedResponse = new SearchResponse<>(users, 2L);

        when(userManagementRepository.search(form)).thenReturn(expectedResponse);

        SearchResponse<User> actualResponse = userManagementService.search(form);

        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.getItems().size());
        verify(userManagementRepository, times(1)).search(form);
    }
}

