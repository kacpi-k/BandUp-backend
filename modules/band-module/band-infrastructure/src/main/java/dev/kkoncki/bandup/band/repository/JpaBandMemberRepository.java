package dev.kkoncki.bandup.band.repository;

import dev.kkoncki.bandup.band.BandMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaBandMemberRepository extends JpaRepository<BandMemberEntity, String> {
    List<BandMemberEntity> findAllByBandId(String bandId);
    List<BandMemberEntity> findAllByUserId(String userId);
}
