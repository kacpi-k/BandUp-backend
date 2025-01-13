package dev.kkoncki.bandup.user.management.instrument.user.instrument;

import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.UserManagementRepositoryMock;
import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import dev.kkoncki.bandup.user.management.instrument.SkillLevel;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.CreateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.UpdateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.repository.UserInstrumentRepository;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.service.UserInstrumentService;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.service.UserInstrumentServiceImpl;
import dev.kkoncki.bandup.user.management.repository.UserManagementRepository;
import dev.kkoncki.bandup.user.management.service.UserManagementService;
import dev.kkoncki.bandup.user.management.service.UserManagementServiceImpl;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserInstrumentServiceTest {

    UserInstrumentRepository userInstrumentRepository = new UserInstrumentRepositoryMock();
    UserManagementRepository userManagementRepository = new UserManagementRepositoryMock();

    UserInstrumentService userInstrumentService = new UserInstrumentServiceImpl(userInstrumentRepository);
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

    private User createUser(String firstName, String lastName, String email) {
        CreateUserForm userForm = new CreateUserForm(firstName, lastName, email);
        return userManagementService.save(userForm);
    }

    private UserInstrument createUserInstrument(String instrumentId, SkillLevel skillLevel, String userId) {
        CreateUserInstrumentForm userInstrumentForm = new CreateUserInstrumentForm(instrumentId, skillLevel);
        return userInstrumentService.save(userInstrumentForm, userId);
    }

    @Test
    void getTest() {
        User user = createUser("Kacper", "Koncki", "test@test.pl");
        UserInstrument userInstrument = createUserInstrument("1", SkillLevel.BEGINNER, user.getId());

        UserInstrument userInstrumentFound = userInstrumentService.get(userInstrument.getId());

        assertEquals(userInstrument.getId(), userInstrumentFound.getId());
        assertEquals(user.getId(), userInstrumentFound.getUserId());
        assertEquals(userInstrument.getInstrumentId(), userInstrumentFound.getInstrumentId());
        assertEquals(userInstrument.getSkillLevel(), userInstrumentFound.getSkillLevel());
    }

    @Test
    void saveTest() {
        User user = createUser("Kacper", "Koncki", "test@test.pl");
        UserInstrument userInstrument = createUserInstrument("1", SkillLevel.BEGINNER, user.getId());

        assertNotNull(userInstrument.getId());
        assertEquals(user.getId(), userInstrument.getUserId());
        assertEquals("1", userInstrument.getInstrumentId());
        assertEquals(SkillLevel.BEGINNER, userInstrument.getSkillLevel());
    }

    @Test
    void updateTest() {
        User user = createUser("Kacper", "Koncki", "test@test.pl");
        UserInstrument userInstrument = createUserInstrument("1", SkillLevel.BEGINNER, user.getId());

        UpdateUserInstrumentForm updateUserInstrumentForm = new UpdateUserInstrumentForm(userInstrument.getId(), "1", SkillLevel.INTERMEDIATE);
        userInstrumentService.update(updateUserInstrumentForm);

        UserInstrument userInstrumentUpdated = userInstrumentService.get(userInstrument.getId());

        assertEquals(userInstrument.getId(), userInstrumentUpdated.getId());
        assertEquals(user.getId(), userInstrumentUpdated.getUserId());
        assertEquals("1", userInstrumentUpdated.getInstrumentId());
        assertEquals(SkillLevel.INTERMEDIATE, userInstrumentUpdated.getSkillLevel());
    }

    @Test
    void getAllByUserIdTest() {
        User user1 = createUser("Kacper", "Koncki", "test@test.pl");
        User user2 = createUser("Jan", "Kowalski", "test2@test.pl");

        UserInstrument userInstrument1 = createUserInstrument("1", SkillLevel.BEGINNER, user1.getId());
        UserInstrument userInstrument2 = createUserInstrument("2", SkillLevel.ADVANCED, user2.getId());
        UserInstrument userInstrument3 = createUserInstrument("1", SkillLevel.EXPERT, user1.getId());

        List<UserInstrument> userInstruments = userInstrumentService.getAllByUserId(user1.getId());

        assertEquals(2, userInstruments.size());
        assertTrue(userInstruments.contains(userInstrument1));
        assertFalse(userInstruments.contains(userInstrument2));
        assertTrue(userInstruments.contains(userInstrument3));
    }

    @Test
    void validateCreateUserInstrumentFormTest() {
        CreateUserInstrumentForm userInstrumentForm = new CreateUserInstrumentForm("1", SkillLevel.BEGINNER);

        genericViolationSet(userInstrumentForm);
    }

    @Test
    void validateUpdateUserInstrumentFormTest() {
        UpdateUserInstrumentForm updateUserInstrumentForm = new UpdateUserInstrumentForm("1", "1", SkillLevel.BEGINNER);

        genericViolationSet(updateUserInstrumentForm);
    }
}
