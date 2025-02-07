package dev.kkoncki.bandup.user.management.instrument.user.instrument.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrument;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.CreateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.forms.UpdateUserInstrumentForm;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.repository.UserInstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserInstrumentServiceImpl implements UserInstrumentService {

    private final UserInstrumentRepository userInstrumentRepository;

    public UserInstrumentServiceImpl(UserInstrumentRepository userInstrumentRepository) {
        this.userInstrumentRepository = userInstrumentRepository;
    }

    private UserInstrument getOrThrowUserInstrument(String id) {
        return userInstrumentRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.USER_INSTRUMENT_NOT_FOUND));
    }

    @Override
    public UserInstrument save(CreateUserInstrumentForm form, String userId) {
        boolean exists = userInstrumentRepository.existsByUserIdAndInstrumentId(userId, form.getInstrumentId());

        if (exists) {
            throw new ApplicationException(ErrorCode.USER_INSTRUMENT_ALREADY_EXISTS);
        }

        UserInstrument userInstrument = UserInstrument.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .instrumentId(form.getInstrumentId())
                .skillLevel(form.getSkillLevel())
                .build();
        return userInstrumentRepository.save(userInstrument);
    }

    @Override
    public void update(UpdateUserInstrumentForm form) {
        UserInstrument userInstrument = getOrThrowUserInstrument(form.getId());
        userInstrument.setInstrumentId(form.getInstrumentId());
        userInstrument.setSkillLevel(form.getSkillLevel());
        userInstrumentRepository.save(userInstrument);
    }

    @Override
    public UserInstrument get(String id) {
        return getOrThrowUserInstrument(id);
    }

    @Override
    public List<UserInstrument> getAllByUserId(String userId) {
        return userInstrumentRepository.findAllByUserId(userId);
    }

    @Override
    public void delete(String id) {
        userInstrumentRepository.delete(id);
    }
}
