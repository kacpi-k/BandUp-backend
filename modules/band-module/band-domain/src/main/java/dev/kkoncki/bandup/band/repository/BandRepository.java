package dev.kkoncki.bandup.band.repository;

import dev.kkoncki.bandup.band.Band;
import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.commons.search.SearchResponse;

import java.util.List;
import java.util.Optional;

public interface BandRepository {
    Optional<Band> findById(String id);
    Band save(Band band);
    void delete(String id);
    List<Band> findAll();
    SearchResponse<Band> search(SearchForm form);
    List<Band> findAllByMemberUserId(String userId);
}
