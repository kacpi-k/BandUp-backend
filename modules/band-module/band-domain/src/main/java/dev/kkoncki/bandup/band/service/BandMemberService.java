package dev.kkoncki.bandup.band.service;

import dev.kkoncki.bandup.band.BandMember;
import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.UpdateBandMemberRoleForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface BandMemberService {
    BandMember get(String id);
    List<BandMember> getAllMembersByBandId(String bandId);
    BandMember addMember(@Valid AddBandMemberForm form);
    void updateMemberRole(@Valid UpdateBandMemberRoleForm form);
    void removeMember(String memberId);
}
