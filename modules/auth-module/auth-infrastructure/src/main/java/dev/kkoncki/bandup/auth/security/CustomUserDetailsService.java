package dev.kkoncki.bandup.auth.security;

import dev.kkoncki.bandup.security.JwtAuthUser;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(user -> JwtAuthUser.builder()
                        .id(user.getId())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
