package dev.kkoncki.bandup.band.repository;

import dev.kkoncki.bandup.band.Band;
import dev.kkoncki.bandup.band.BandEntity;
import dev.kkoncki.bandup.band.BandMapper;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.commons.genre.Genre;
import dev.kkoncki.bandup.commons.genre.GenreEntity;
import dev.kkoncki.bandup.commons.genre.GenreMapper;
import dev.kkoncki.bandup.commons.genre.repository.GenreRepository;
import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.commons.search.SearchResponse;
import dev.kkoncki.bandup.commons.search.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BandRepositoryAdapter implements BandRepository {

    private final JpaBandRepository jpaBandRepository;
    private final GenreRepository genreRepository;

    public BandRepositoryAdapter(JpaBandRepository jpaBandRepository, GenreRepository genreRepository) {
        this.jpaBandRepository = jpaBandRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Optional<Band> findById(String id) {
        Optional<BandEntity> bandEntity = jpaBandRepository.findById(id);
        return bandEntity.map(BandMapper::toDomain);
    }

    @Override
    public Band save(Band band) {
        List<Genre> genres = band.getGenres().stream()
                .map(genreId -> genreRepository.findById(genreId)
                        .orElseThrow(() -> new ApplicationException(ErrorCode.GENRE_NOT_FOUND)))
                .toList();

        List<GenreEntity> genreEntities = genres.stream()
                .map(GenreMapper::toEntity)
                .toList();

        BandEntity bandEntity = BandMapper.toEntity(band, genreEntities);

        BandEntity savedEntity = jpaBandRepository.save(bandEntity);

        return BandMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(String id) {
        BandEntity bandEntity = jpaBandRepository.findById(id).orElseThrow();
        jpaBandRepository.delete(bandEntity);
    }

    @Override
    public List<Band> findAll() {
        return jpaBandRepository.findAll().stream()
                .map(BandMapper::toDomain)
                .toList();
    }

    @Override
    public SearchResponse<Band> search(SearchForm form) {
        Specification<BandEntity> specification = SearchSpecification.buildSpecification(form.getCriteria());
        Page<BandEntity> bandPage = jpaBandRepository.findAll(specification, SearchSpecification.getPageRequest(form));
        return SearchResponse.<Band>builder()
                .items(bandPage.getContent().stream()
                        .map(BandMapper::toDomain)
                        .collect(Collectors.toList()))
                .total(bandPage.getTotalElements())
                .build();
    }

    @Override
    public List<Band> findAllByMemberUserId(String userId) {
        return jpaBandRepository.findAllByMemberUserId(userId).stream()
                .map(BandMapper::toDomain)
                .toList();
    }
}
