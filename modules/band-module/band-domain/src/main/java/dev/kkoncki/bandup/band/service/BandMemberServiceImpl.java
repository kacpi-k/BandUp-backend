package dev.kkoncki.bandup.band.service;

import dev.kkoncki.bandup.band.BandMember;
import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.UpdateBandMemberRoleForm;
import dev.kkoncki.bandup.band.repository.BandMemberRepository;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class BandMemberServiceImpl implements BandMemberService {

    private final BandMemberRepository bandMemberRepository;
    private final BandHelperService bandHelperService;

    public BandMemberServiceImpl(BandMemberRepository bandMemberRepository, BandHelperService bandHelperService) {
        this.bandMemberRepository = bandMemberRepository;
        this.bandHelperService = bandHelperService;
    }

    private BandMember getBandMemberOrThrow(String memberId) {
        return bandMemberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BAND_MEMBER_NOT_FOUND));
    }

    @Override
    public BandMember get(String id) {
        return getBandMemberOrThrow(id);
    }

    @Override
    public List<BandMember> getAllMembersByBandId(String bandId) {
        bandHelperService.getBandOrThrow(bandId);
        return bandMemberRepository.findAllByBandId(bandId);
    }

    @Override
    public BandMember addMember(AddBandMemberForm form) {
        bandHelperService.getBandOrThrow(form.getBandId());

        BandMember newMember = BandMember.builder()
                .id(UUID.randomUUID().toString())
                .bandId(form.getBandId())
                .userId(form.getUserId())
                .role(form.getRole())
                .joinedOn(Instant.now())
                .build();

        return bandMemberRepository.save(newMember);
    }

    @Override
    public void updateMemberRole(UpdateBandMemberRoleForm form) {
        BandMember member = getBandMemberOrThrow(form.getMemberId());

        member.setRole(form.getNewRole());

        bandMemberRepository.save(member);
    }

    @Override
    public void removeMember(String memberId) {
        getBandMemberOrThrow(memberId);
        bandMemberRepository.delete(memberId);
    }
}
