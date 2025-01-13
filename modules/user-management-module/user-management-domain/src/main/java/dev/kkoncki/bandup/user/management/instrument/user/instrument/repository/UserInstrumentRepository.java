package dev.kkoncki.bandup.user.management.instrument.user.instrument.repository;

import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrument;

import java.util.List;
import java.util.Optional;

public interface UserInstrumentRepository {
    Optional<UserInstrument> findById(String id);
    UserInstrument save(UserInstrument userInstrument);
    List<UserInstrument> findAllByUserId(String userId);
}
