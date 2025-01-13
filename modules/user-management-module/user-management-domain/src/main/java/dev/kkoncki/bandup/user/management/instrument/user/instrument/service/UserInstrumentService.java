package dev.kkoncki.bandup.user.management.instrument.user.instrument.service;

import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrument;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.CreateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.UpdateUserInstrumentForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface UserInstrumentService {
    UserInstrument save(@Valid CreateUserInstrumentForm form, String userId);
    void update(@Valid UpdateUserInstrumentForm form);
    UserInstrument get(String id);
    List<UserInstrument> getAllByUserId(String userId);
}
