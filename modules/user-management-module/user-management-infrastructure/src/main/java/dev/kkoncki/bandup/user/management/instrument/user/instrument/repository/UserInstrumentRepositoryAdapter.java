package dev.kkoncki.bandup.user.management.instrument.user.instrument.repository;

import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrument;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrumentEntity;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrumentMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserInstrumentRepositoryAdapter implements UserInstrumentRepository {

    private final JpaUserInstrumentRepository jpaUserInstrumentRepository;

    public UserInstrumentRepositoryAdapter(JpaUserInstrumentRepository jpaUserInstrumentRepository) {
        this.jpaUserInstrumentRepository = jpaUserInstrumentRepository;
    }

    @Override
    public Optional<UserInstrument> findById(String id) {
        return jpaUserInstrumentRepository.findById(id).map(UserInstrumentMapper::toDomain);
    }

    @Override
    public UserInstrument save(UserInstrument userInstrument) {
        UserInstrumentEntity entity = UserInstrumentMapper.toEntity(userInstrument);
        return UserInstrumentMapper.toDomain(jpaUserInstrumentRepository.save(entity));
    }

    @Override
    public List<UserInstrument> findAllByUserId(String userId) {
        return jpaUserInstrumentRepository.findAllByUser_Id(userId).stream()
                .map(UserInstrumentMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByUserIdAndInstrumentId(String userId, String instrumentId) {
        return jpaUserInstrumentRepository.existsByUserIdAndInstrumentId(userId, instrumentId);
    }

    @Override
    public void delete(String id) {
        jpaUserInstrumentRepository.deleteById(id);
    }
}
