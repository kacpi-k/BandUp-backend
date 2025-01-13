package dev.kkoncki.bandup.user.management.instrument.user.instrument;

import dev.kkoncki.bandup.user.management.instrument.user.instrument.repository.UserInstrumentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserInstrumentRepositoryMock implements UserInstrumentRepository {

    Map<String, UserInstrument> mockDB = new HashMap<>();

    @Override
    public Optional<UserInstrument> findById(String id) {
        return Optional.ofNullable(mockDB.get(id));
    }

    @Override
    public UserInstrument save(UserInstrument userInstrument) {
        mockDB.put(userInstrument.getId(), userInstrument);
        return userInstrument;
    }

    @Override
    public List<UserInstrument> findAllByUserId(String userId) {
        return mockDB.values().stream()
                .filter(userInstrument -> userInstrument.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
