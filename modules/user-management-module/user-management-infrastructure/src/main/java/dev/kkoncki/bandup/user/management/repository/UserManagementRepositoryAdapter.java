package dev.kkoncki.bandup.user.management.repository;

import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.UserEntity;
import dev.kkoncki.bandup.user.management.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserManagementRepositoryAdapter implements UserManagementRepository {

    private final JpaUserManagementRepository jpaUserManagementRepository;

    public UserManagementRepositoryAdapter(JpaUserManagementRepository jpaUserManagementRepository) {
        this.jpaUserManagementRepository = jpaUserManagementRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return jpaUserManagementRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        return UserMapper.toDomain(jpaUserManagementRepository.save(entity));
    }
}
