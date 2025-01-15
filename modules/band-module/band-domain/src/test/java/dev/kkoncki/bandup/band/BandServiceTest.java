package dev.kkoncki.bandup.band;

import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.CreateBandForm;
import dev.kkoncki.bandup.band.forms.UpdateBandForm;
import dev.kkoncki.bandup.band.repository.BandRepository;
import dev.kkoncki.bandup.band.service.BandHelperService;
import dev.kkoncki.bandup.band.service.BandMemberService;
import dev.kkoncki.bandup.band.service.BandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BandServiceTest {

    @Mock
    private BandRepository bandRepository;

    @Mock
    private BandMemberService bandMemberService;

    @Mock
    private BandHelperService bandHelperService;

    @InjectMocks
    private BandServiceImpl bandService;


    private CreateBandForm createBandForm;
    private UpdateBandForm updateBandForm;

    @BeforeEach
    void setUp() {
        createBandForm = new CreateBandForm("Test Band", "A great band", List.of("rock", "pop"));
        updateBandForm = new UpdateBandForm("band-id", "Updated Band", "An even better band", List.of("jazz"));
    }

    @Test
    void shouldCreateBand() {
        Band band = new Band("band-id", "Test Band", "A great band", Instant.now(), new ArrayList<>(), List.of("rock", "pop"));
        when(bandRepository.save(any(Band.class))).thenReturn(band);

        Band result = bandService.createBand(createBandForm, "leader-id");


        assertEquals("band-id", result.getId());
        assertEquals("Test Band", result.getName());
        assertEquals("A great band", result.getDescription());
        assertEquals(List.of("rock", "pop"), result.getGenres());
        verify(bandMemberService, times(1)).addMember(any(AddBandMemberForm.class));
        verify(bandRepository, times(1)).save(any(Band.class));
    }

    @Test
    void shouldUpdateBand() {
        Band band = new Band("band-id", "Old Name", "Old Description", Instant.now(), new ArrayList<>(), List.of("rock"));
        when(bandHelperService.getBandOrThrow("band-id")).thenReturn(band);
        when(bandRepository.save(any(Band.class))).thenReturn(band);

        Band result = bandService.updateBand(updateBandForm);

        assertEquals("Updated Band", result.getName());
        assertEquals("An even better band", result.getDescription());
        assertEquals(List.of("jazz"), result.getGenres());
        verify(bandHelperService, times(1)).getBandOrThrow("band-id");
        verify(bandRepository, times(1)).save(any(Band.class));
    }

    @Test
    void shouldDeleteBand() {
        Band band = new Band("band-id", "Test Band", "A great band", Instant.now(), new ArrayList<>(), List.of("rock", "pop"));
        when(bandHelperService.getBandOrThrow("band-id")).thenReturn(band);
        when(bandMemberService.getAllMembersByBandId("band-id")).thenReturn(new ArrayList<>());

        bandService.deleteBand("band-id");

        verify(bandHelperService, times(1)).getBandOrThrow("band-id");
        verify(bandMemberService, times(1)).getAllMembersByBandId("band-id");
        verify(bandRepository, times(1)).delete("band-id");
    }
}

