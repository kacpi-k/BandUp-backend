package dev.kkoncki.bandup.user.management.repository;

import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.commons.search.SearchResponse;
import dev.kkoncki.bandup.user.management.User;

import java.util.Optional;

public interface UserManagementRepository {
    Optional<User> findById(String id);
    User save(User user);
    SearchResponse<User> search(SearchForm form);
    void updateBio(String userId, String bio);
    void updateImageUrl(String userId, String imageUrl);
}
