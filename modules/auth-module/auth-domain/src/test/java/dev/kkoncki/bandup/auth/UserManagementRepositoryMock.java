package dev.kkoncki.bandup.auth;

import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.repository.UserManagementRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserManagementRepositoryMock implements UserManagementRepository {

    Map<String, User> mockDB = new HashMap<>();

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(mockDB.get(id));
    }

    @Override
    public User save(User user) {
        mockDB.put(user.getId(), user);
        return user;
    }
}
