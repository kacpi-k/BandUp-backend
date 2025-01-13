package dev.kkoncki.bandup.user.management.repository;

import dev.kkoncki.bandup.user.management.User;

import java.util.Optional;

public interface UserManagementRepository {
    Optional<User> findById(String id);
    User save(User user);
}
