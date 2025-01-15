package dev.kkoncki.bandup.band;

import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.UpdateBandMemberRoleForm;
import dev.kkoncki.bandup.band.repository.BandMemberRepository;
import dev.kkoncki.bandup.band.service.BandHelperService;
import dev.kkoncki.bandup.band.service.BandMemberServiceImpl;
import dev.kkoncki.bandup.band.service.BandService;
import dev.kkoncki.bandup.commons.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BandMemberServiceTest {

    @Mock
    private BandMemberRepository bandMemberRepository;

    @Mock
    private BandHelperService bandHelperService;

    @InjectMocks
    private BandMemberServiceImpl bandMemberService;

    private AddBandMemberForm addBandMemberForm;
    private UpdateBandMemberRoleForm updateBandMemberRoleForm;

    @BeforeEach
    void setUp() {
        addBandMemberForm = new AddBandMemberForm("band-id", "user-id", "member");
        updateBandMemberRoleForm = new UpdateBandMemberRoleForm("member-id", "leader");
    }

    @Test
    void shouldGetBandMember() {
        BandMember member = new BandMember("member-id", "band-id", "user-id", "member", Instant.now());
        when(bandMemberRepository.findById("member-id")).thenReturn(Optional.of(member));

        BandMember result = bandMemberService.get("member-id");

        assertEquals(member, result);
        verify(bandMemberRepository, times(1)).findById("member-id");
    }

    @Test
    void shouldGetAllMembersByBandId() {
        Band band = new Band("band-id", "Test Band", "description", Instant.now(), new ArrayList<>(), List.of("rock"));
        List<BandMember> members = List.of(
                new BandMember("member-1", "band-id", "user-1", "member", Instant.now()),
                new BandMember("member-2", "band-id", "user-2", "leader", Instant.now())
        );

        when(bandHelperService.getBandOrThrow("band-id")).thenReturn(band);
        when(bandMemberRepository.findAllByBandId("band-id")).thenReturn(members);

        List<BandMember> result = bandMemberService.getAllMembersByBandId("band-id");

        assertEquals(2, result.size());
        assertTrue(result.containsAll(members));
        verify(bandHelperService, times(1)).getBandOrThrow("band-id");
        verify(bandMemberRepository, times(1)).findAllByBandId("band-id");
    }


    @Test
    void shouldAddMember() {
        Band band = new Band("band-id", "Test Band", "description", Instant.now(), new ArrayList<>(), List.of("rock"));
        BandMember newMember = new BandMember("generated-id", "band-id", "user-id", "member", Instant.now());

        when(bandHelperService.getBandOrThrow("band-id")).thenReturn(band);
        when(bandMemberRepository.save(any(BandMember.class))).thenReturn(newMember);

        BandMember result = bandMemberService.addMember(addBandMemberForm);

        assertNotNull(result);
        assertEquals("band-id", result.getBandId());
        assertEquals("user-id", result.getUserId());
        assertEquals("member", result.getRole());
        verify(bandHelperService, times(1)).getBandOrThrow("band-id");
        verify(bandMemberRepository, times(1)).save(any(BandMember.class));
    }


    @Test
    void shouldUpdateMemberRole() {
        BandMember existingMember = new BandMember("member-id", "band-id", "user-id", "member", Instant.now());
        when(bandMemberRepository.findById("member-id")).thenReturn(Optional.of(existingMember));

        bandMemberService.updateMemberRole(updateBandMemberRoleForm);

        assertEquals("leader", existingMember.getRole());
        verify(bandMemberRepository, times(1)).findById("member-id");
        verify(bandMemberRepository, times(1)).save(existingMember);
    }

    @Test
    void shouldRemoveMember() {
        BandMember member = new BandMember("member-id", "band-id", "user-id", "member", Instant.now());
        when(bandMemberRepository.findById("member-id")).thenReturn(Optional.of(member));

        bandMemberService.removeMember("member-id");

        verify(bandMemberRepository, times(1)).findById("member-id");
        verify(bandMemberRepository, times(1)).delete("member-id");
    }

    @Test
    void shouldThrowExceptionWhenMemberNotFound() {
        when(bandMemberRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> bandMemberService.get("non-existent-id"));
        verify(bandMemberRepository, times(1)).findById("non-existent-id");
    }

}
