package dev.kkoncki.bandup.user.management.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import dev.kkoncki.bandup.user.management.repository.UserManagementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final UserManagementRepository userManagementRepository;

    public UserManagementServiceImpl(UserManagementRepository userManagementRepository) {
        this.userManagementRepository = userManagementRepository;
    }

    private User getOrThrowUser(String id) {
        return userManagementRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User get(String id) {
        return getOrThrowUser(id);
    }

    @Override
    public User save(CreateUserForm form) {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .email(form.getEmail())
                .createdOn(Instant.now())
                .isBlocked(false)
                .instruments(new ArrayList<>())
                .bio(null)
                .genres(new ArrayList<>())
                .build();

        return userManagementRepository.save(user);
    }

    @Override
    public User block(String id) {
        User user = getOrThrowUser(id);
        user.setBlocked(true);
        return userManagementRepository.save(user);
    }

    @Override
    public User unBlock(String id) {
        User user = getOrThrowUser(id);
        user.setBlocked(false);
        return userManagementRepository.save(user);
    }

    @Override
    public void addOrRemoveInstrument(String userInstrumentId, String userId) {
        User user = get(userId);
        if (user.getInstruments().contains(userInstrumentId)) {
            user.getInstruments().remove(userInstrumentId);
        } else {
            user.getInstruments().add(userInstrumentId);
        }

        // TODO remove in db
    }

    @Override
    public void addOrRemoveGenre(String genreId, String userId) {
        User user = get(userId);
        if (user.getGenres().contains(genreId)) {
            user.getGenres().remove(genreId);
        } else {
            user.getGenres().add(genreId);
        }
    }
}
