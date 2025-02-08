package dev.kkoncki.bandup.band.service;

import dev.kkoncki.bandup.band.Band;
import dev.kkoncki.bandup.band.BandMember;
import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.CreateBandForm;
import dev.kkoncki.bandup.band.forms.UpdateBandForm;
import dev.kkoncki.bandup.band.repository.BandRepository;
import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.commons.search.SearchResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BandServiceImpl implements BandService {

    private final BandRepository bandRepository;
    private final BandMemberService bandMemberService;
    private final BandHelperService bandHelperService;

    public BandServiceImpl(BandRepository bandRepository, BandMemberService bandMemberService, BandHelperService bandHelperService) {
        this.bandRepository = bandRepository;
        this.bandMemberService = bandMemberService;
        this.bandHelperService = bandHelperService;
    }

/*    private void verifyLeader(String bandId, String leaderId) {
        List<BandMember> members = bandMemberService.getAllMembersByBandId(bandId);

        boolean isLeader = members.stream()
                .anyMatch(member -> member.getUserId().equals(leaderId) && member.isLeader());

        if (!isLeader) {
            throw new ApplicationException(ErrorCode.NOT_A_LEADER);
        }
    }*/

    @Override
    public Band createBand(CreateBandForm form, String leaderId) {
        Band band = Band.builder()
                .id(UUID.randomUUID().toString())
                .name(form.getName())
                .description(form.getDescription())
                .createdOn(Instant.now())
                .members(new ArrayList<>())
                .genres(form.getGenres())
                .build();

        Band savedBand = bandRepository.save(band);

        AddBandMemberForm leaderForm = new AddBandMemberForm(savedBand.getId(), leaderId, "leader");
        bandMemberService.addMember(leaderForm);

        return savedBand;
    }

    @Override
    public Band get(String id) {
        return bandHelperService.getBandOrThrow(id);
    }

    @Override
    public Band updateBand(UpdateBandForm form) {
        Band band = get(form.getId());

        band.setName(form.getName());
        band.setDescription(form.getDescription());
        band.updateGenres(form.getGenres());

        return bandRepository.save(band);
    }

    @Override
    public void deleteBand(String id) {
        get(id);
        List<BandMember> members = bandMemberService.getAllMembersByBandId(id);
        for (BandMember member : members) {
            bandMemberService.removeMember(member.getId());
        }
        bandRepository.delete(id);
    }

    @Override
    public List<Band> getAllBands() {
        return bandRepository.findAll();
    }

    @Override
    public SearchResponse<Band> search(SearchForm form) {
        return bandRepository.search(form);
    }
}
