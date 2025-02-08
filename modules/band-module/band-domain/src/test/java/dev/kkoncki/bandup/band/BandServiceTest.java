package dev.kkoncki.bandup.band;

import dev.kkoncki.bandup.band.forms.AddBandMemberForm;
import dev.kkoncki.bandup.band.forms.CreateBandForm;
import dev.kkoncki.bandup.band.forms.UpdateBandForm;
import dev.kkoncki.bandup.band.repository.BandRepository;
import dev.kkoncki.bandup.band.service.BandHelperService;
import dev.kkoncki.bandup.band.service.BandMemberService;
import dev.kkoncki.bandup.band.service.BandServiceImpl;
import dev.kkoncki.bandup.commons.search.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    private Band band1;
    private Band band2;
    private SearchForm searchForm;

    @BeforeEach
    void setUp() {
        createBandForm = new CreateBandForm("Test Band", "A great band", List.of("rock", "pop"));
        updateBandForm = new UpdateBandForm("band-id", "Updated Band", "An even better band", List.of("jazz"));

        band1 = new Band("band-123", "Rock Legends", "A legendary rock band", Instant.now(), new ArrayList<>(), List.of("rock", "classic"));
        band2 = new Band("band-456", "Jazz Masters", "Smooth jazz and blues", Instant.now(), new ArrayList<>(), List.of("jazz", "blues"));

        searchForm = new SearchForm(
                List.of(new SearchFormCriteria("name", "Rock", CriteriaOperator.LIKE)),
                1, 10, new SearchSort("createdOn", SearchSortOrder.ASC)
        );
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

    @Test
    void shouldSearchBandsSuccessfully() {
        List<Band> bands = List.of(band1);
        SearchResponse<Band> expectedResponse = new SearchResponse<>(bands, 1L);

        when(bandRepository.search(searchForm)).thenReturn(expectedResponse);

        SearchResponse<Band> result = bandService.search(searchForm);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Rock Legends", result.getItems().get(0).getName());

        verify(bandRepository, times(1)).search(searchForm);
    }

    @Test
    void shouldReturnEmptyListWhenNoBandsFound() {
        SearchResponse<Band> expectedResponse = new SearchResponse<>(List.of(), 0L);

        when(bandRepository.search(searchForm)).thenReturn(expectedResponse);

        SearchResponse<Band> result = bandService.search(searchForm);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());

        verify(bandRepository, times(1)).search(searchForm);
    }

    @Test
    void shouldSearchBandsByGenre() {
        SearchForm genreSearchForm = new SearchForm(
                List.of(new SearchFormCriteria("genres", "jazz", CriteriaOperator.LIKE)),
                1, 10, new SearchSort("createdOn", SearchSortOrder.ASC)
        );

        List<Band> bands = List.of(band2);
        SearchResponse<Band> expectedResponse = new SearchResponse<>(bands, 1L);

        when(bandRepository.search(genreSearchForm)).thenReturn(expectedResponse);

        SearchResponse<Band> result = bandService.search(genreSearchForm);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Jazz Masters", result.getItems().get(0).getName());

        verify(bandRepository, times(1)).search(genreSearchForm);
    }
}

