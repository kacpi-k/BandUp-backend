package dev.kkoncki.bandup.user.management.service;

import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import dev.kkoncki.bandup.user.management.forms.UpdateUserLocationForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserManagementService {
    User get(String id);
    User save(@Valid CreateUserForm form);
    User block(String id);
    User unBlock(String id);
    void addUserInstrument(String userInstrumentId, String userId);
    void removeUserInstrument(String userInstrumentId, String userId);
    void addOrRemoveGenre(String genreId, String userId);
    void updateUserLocation(String userId, @Valid UpdateUserLocationForm form);
    void updateBio(String userId, String bio);
    void updateImageUrl(String userId, String imageUrl);
}
