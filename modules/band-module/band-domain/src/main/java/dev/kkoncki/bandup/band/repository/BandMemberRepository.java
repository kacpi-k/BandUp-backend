package dev.kkoncki.bandup.band.repository;

import dev.kkoncki.bandup.band.BandMember;

import java.util.List;
import java.util.Optional;

public interface BandMemberRepository {
    Optional<BandMember> findById(String id);
    List<BandMember> findAllByBandId(String bandId);
    BandMember save(BandMember bandMember);
    void delete(String id);
    List<BandMember> findAllByUserId(String userId);
}
