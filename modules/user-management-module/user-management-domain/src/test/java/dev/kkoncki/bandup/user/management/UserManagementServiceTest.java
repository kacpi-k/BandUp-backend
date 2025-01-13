package dev.kkoncki.bandup.user.management;

import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import dev.kkoncki.bandup.user.management.repository.UserManagementRepository;
import dev.kkoncki.bandup.user.management.service.UserManagementService;
import dev.kkoncki.bandup.user.management.service.UserManagementServiceImpl;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementServiceTest {

    UserManagementRepository userManagementRepository = new UserManagementRepositoryMock();

    UserManagementService userManagementService = new UserManagementServiceImpl(userManagementRepository);


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
        CreateUserForm form = new CreateUserForm("Kacper", "Koncki", "test@test.pl");

        User user = userManagementService.save(form);

        User userFound = userManagementService.get(user.getId());

        assertEquals(user.getId(), userFound.getId());
        assertEquals(user.getFirstName(), userFound.getFirstName());
        assertEquals(user.getLastName(), userFound.getLastName());
        assertEquals(user.getEmail(), userFound.getEmail());
    }

    @Test
    void saveTest() {
        CreateUserForm form = new CreateUserForm("Kacper", "Koncki", "test@test.pl");

        User user = userManagementService.save(form);

        assertNotNull(user.getId());
        assertEquals("Kacper", user.getFirstName());
        assertEquals("Koncki", user.getLastName());
        assertEquals("test@test.pl", user.getEmail());
    }

    @Test
    void blockTest() {
        CreateUserForm form = new CreateUserForm("Kacper", "Koncki", "test@test.pl");

        User user = userManagementService.save(form);

        userManagementService.block(user.getId());

        assertTrue(user.isBlocked());
    }

    @Test
    void unBlockTest() {
        CreateUserForm form = new CreateUserForm("Kacper", "Koncki", "test@test.pl");

        User user = userManagementService.save(form);

        userManagementService.block(user.getId());
        userManagementService.unBlock(user.getId());

        assertFalse(user.isBlocked());
    }

    @Test
    void addOrRemoveInstrumentTest() {
        CreateUserForm form = new CreateUserForm("Kacper", "Koncki", "test@test.pl");
        User user = userManagementService.save(form);

        String userInstrumentId = "instrument1";

        userManagementService.addOrRemoveInstrument(userInstrumentId, user.getId());

        User updatedUser = userManagementService.get(user.getId());
        assertTrue(updatedUser.getInstruments().contains(userInstrumentId));

        userManagementService.addOrRemoveInstrument(userInstrumentId, user.getId());

        updatedUser = userManagementService.get(user.getId());
        assertFalse(updatedUser.getInstruments().contains(userInstrumentId));
    }

    @Test
    void addOrRemoveGenreTest() {
        CreateUserForm form = new CreateUserForm("Kacper", "Koncki", "test@test.pl");
        User user = userManagementService.save(form);

        String genreId = "genre1";

        userManagementService.addOrRemoveGenre(genreId, user.getId());

        User updatedUser = userManagementService.get(user.getId());
        assertTrue(updatedUser.getGenres().contains(genreId));

        userManagementService.addOrRemoveGenre(genreId, user.getId());

        updatedUser = userManagementService.get(user.getId());
        assertFalse(updatedUser.getGenres().contains(genreId));
    }

    @Test
    void validateCreateUserForm() {
        CreateUserForm form = new CreateUserForm("K", "T", "test");

        genericViolationSet(form);
    }
}
