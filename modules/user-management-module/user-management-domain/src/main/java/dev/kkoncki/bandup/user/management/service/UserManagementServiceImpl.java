package dev.kkoncki.bandup.user.management.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.commons.search.SearchResponse;
import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import dev.kkoncki.bandup.user.management.forms.UpdateUserLocationForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.service.UserInstrumentService;
import dev.kkoncki.bandup.user.management.repository.UserManagementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final UserInstrumentService userInstrumentService;

    public UserManagementServiceImpl(UserManagementRepository userManagementRepository, UserInstrumentService userInstrumentService) {
        this.userManagementRepository = userManagementRepository;
        this.userInstrumentService = userInstrumentService;
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
                .imageUrl(null)
                .latitude(null)
                .longitude(null)
                .city(null)
                .country(null)
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
    public void addUserInstrument(String userInstrumentId, String userId) {
        User user = get(userId);

        if (user.getInstruments().contains(userInstrumentId)) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_HAS_INSTRUMENT);
        }

        user.getInstruments().add(userInstrumentId);
        userManagementRepository.save(user);
    }

    @Override
    public void removeUserInstrument(String userInstrumentId, String userId) {
        User user = get(userId);
        if (!user.getInstruments().contains(userInstrumentId)) {
            throw new ApplicationException(ErrorCode.USER_DOES_NOT_HAVE_INSTRUMENT);
        }

        user.getInstruments().remove(userInstrumentId);
        userManagementRepository.save(user);
        userInstrumentService.delete(userInstrumentId);
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

    @Override
    public void updateUserLocation(String userId, UpdateUserLocationForm form) {
        User user = get(userId);
        user.setLatitude(form.getLatitude());
        user.setLongitude(form.getLongitude());
        user.setCity(form.getCity());
        user.setCountry(form.getCountry());
        userManagementRepository.save(user);
    }

    @Override
    public void updateBio(String userId, String bio) {
        User user = get(userId);
        user.setBio(bio);
        userManagementRepository.save(user);
    }

    @Override
    public void updateImageUrl(String userId, String imageUrl) {
        User user = get(userId);
        user.setImageUrl(imageUrl);
        userManagementRepository.save(user);
    }

    @Override
    public SearchResponse<User> search(SearchForm form) {
        return userManagementRepository.search(form);
    }
}
