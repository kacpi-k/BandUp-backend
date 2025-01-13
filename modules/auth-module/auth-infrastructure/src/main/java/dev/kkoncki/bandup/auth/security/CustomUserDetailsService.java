package dev.kkoncki.bandup.auth.security;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.security.JwtAuthUser;
import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.repository.UserManagementRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserManagementRepository repository;


    public CustomUserDetailsService(UserManagementRepository repository) {
        this.repository = repository;
    }

    private User getOrThrow(String id) {
        return repository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = getOrThrow(id);
        return JwtAuthUser.builder()
                .id(user.getId())
                .locked(user.isBlocked())
                .build();
    }
}
