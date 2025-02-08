package dev.kkoncki.bandup.user.management.repository;

import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.commons.search.SearchResponse;
import dev.kkoncki.bandup.commons.search.SearchSpecification;
import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.UserEntity;
import dev.kkoncki.bandup.user.management.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserManagementRepositoryAdapter implements UserManagementRepository {

    private final JpaUserManagementRepository jpaUserManagementRepository;

    public UserManagementRepositoryAdapter(JpaUserManagementRepository jpaUserManagementRepository) {
        this.jpaUserManagementRepository = jpaUserManagementRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return jpaUserManagementRepository.findById(id).map(UserMapper::toDomain);
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
}
