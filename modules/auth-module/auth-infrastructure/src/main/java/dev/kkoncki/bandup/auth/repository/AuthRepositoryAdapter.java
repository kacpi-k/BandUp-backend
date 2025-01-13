package dev.kkoncki.bandup.auth.repository;

import dev.kkoncki.bandup.auth.AuthUser;
import dev.kkoncki.bandup.auth.AuthUserEntity;
import dev.kkoncki.bandup.auth.AuthUserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthRepositoryAdapter implements AuthRepository {

    private final JpaAuthRepository authRepository;

    public AuthRepositoryAdapter(JpaAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        Optional<AuthUserEntity> userEntity = authRepository.findByEmail(email);
        return userEntity.map(AuthUserMapper::toAuthUser);
    }

    @Override
    public Optional<AuthUser> findById(String id) {
        Optional<AuthUserEntity> userEntity = authRepository.findById(id);
        return userEntity.map(AuthUserMapper::toAuthUser);
    }

    @Override
    public void save(AuthUser user) {
        AuthUserEntity userEntity = AuthUserMapper.toAuthUserEntity(user);
        authRepository.save(userEntity);
    }
}
