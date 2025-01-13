package dev.kkoncki.bandup.user.management.service;

import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserManagementService {
    User get(String id);
    User save(@Valid CreateUserForm form);
    User block(String id);
    User unblock(String id);
    void addOrRemoveInstrument(String userInstrumentId, String userId);
    void addOrRemoveGenre(String genreId, String userId);

}
