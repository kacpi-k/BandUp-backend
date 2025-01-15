package dev.kkoncki.bandup.band.repository;

import dev.kkoncki.bandup.band.BandEntity;
import dev.kkoncki.bandup.band.BandMember;
import dev.kkoncki.bandup.band.BandMemberEntity;
import dev.kkoncki.bandup.band.BandMemberMapper;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BandMemberRepositoryAdapter implements BandMemberRepository {

    private final JpaBandMemberRepository jpaBandMemberRepository;
    private final JpaBandRepository jpaBandRepository;

    public BandMemberRepositoryAdapter(JpaBandMemberRepository jpaBandMemberRepository, JpaBandRepository jpaBandRepository) {
        this.jpaBandMemberRepository = jpaBandMemberRepository;
        this.jpaBandRepository = jpaBandRepository;
    }

    @Override
    public Optional<BandMember> findById(String id) {
        return jpaBandMemberRepository.findById(id).map(BandMemberMapper::toDomain);
    }

    @Override
    public List<BandMember> findAllByBandId(String bandId) {
        return jpaBandMemberRepository.findAllByBandId(bandId).stream()
                .map(BandMemberMapper::toDomain)
                .toList();
    }

    @Override
    public BandMember save(BandMember bandMember) {
        BandEntity bandEntity = jpaBandRepository.findById(bandMember.getBandId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.BAND_NOT_FOUND));

        BandMemberEntity entity = BandMemberMapper.toEntity(bandMember, bandEntity);

        BandMemberEntity savedEntity = jpaBandMemberRepository.save(entity);

        return BandMemberMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(String id) {
        if (!jpaBandMemberRepository.existsById(id)) {
            throw new ApplicationException(ErrorCode.BAND_MEMBER_NOT_FOUND);
        }
        jpaBandMemberRepository.deleteById(id);
    }
}
