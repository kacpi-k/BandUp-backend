package dev.kkoncki.bandup.user.management.repository;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.commons.genre.GenreEntity;
import dev.kkoncki.bandup.commons.genre.repository.JpaGenreRepository;
import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.commons.search.SearchResponse;
import dev.kkoncki.bandup.commons.search.SearchSpecification;
import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.UserEntity;
import dev.kkoncki.bandup.user.management.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserManagementRepositoryAdapter implements UserManagementRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final JpaGenreRepository genreRepository;

    private final JpaUserManagementRepository jpaUserManagementRepository;

    public UserManagementRepositoryAdapter(JpaGenreRepository genreRepository, JpaUserManagementRepository jpaUserManagementRepository) {
        this.genreRepository = genreRepository;
        this.jpaUserManagementRepository = jpaUserManagementRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return jpaUserManagementRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAllByIds(List<String> ids) {
        return jpaUserManagementRepository.findAllById(ids).stream()
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        return UserMapper.toDomain(jpaUserManagementRepository.save(entity));
    }

    @Override
    public SearchResponse<User> search(SearchForm form) {
        Specification<UserEntity> specification = SearchSpecification.buildSpecification(form.getCriteria());
        Page<UserEntity> userPage = jpaUserManagementRepository.findAll(specification, SearchSpecification.getPageRequest(form));
        return SearchResponse.<User>builder()
                .items(userPage.getContent().stream()
                        .map(UserMapper::toDomain)
                        .collect(Collectors.toList()))
                .total(userPage.getTotalElements())
                .build();
    }

    @Override
    public void updateBio(String userId, String bio) {
        jpaUserManagementRepository.updateBio(userId, bio);
    }

    @Override
    public void updateImageUrl(String userId, String imageUrl) {
        jpaUserManagementRepository.updateImageUrl(userId, imageUrl);
    }

    @Override
    public void updateGenres(String userId, List<String> genreIds) {
        UserEntity userEntity = entityManager.find(UserEntity.class, userId);

        if (userEntity == null) {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        }

        List<GenreEntity> updatedGenres = genreRepository.findAllById(genreIds);

        userEntity.setGenres(updatedGenres);

        entityManager.merge(userEntity);
    }
}
