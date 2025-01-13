package dev.kkoncki.bandup.user.management.instrument.user.instrument.repository;

import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUserInstrumentRepository extends JpaRepository<UserInstrumentEntity, String> {
    List<UserInstrumentEntity> findAllByUser_Id(String userId);
}
