package dev.kkoncki.bandup.auth.service;

import dev.kkoncki.bandup.auth.AuthUser;
import dev.kkoncki.bandup.auth.LoginResponse;
import dev.kkoncki.bandup.auth.forms.ChangePasswordForm;
import dev.kkoncki.bandup.auth.forms.CreateUserWithPasswordForm;
import dev.kkoncki.bandup.auth.forms.LoginForm;
import dev.kkoncki.bandup.auth.repository.AuthRepository;
import dev.kkoncki.bandup.auth.security.PasswordEncoder;
import dev.kkoncki.bandup.auth.security.TokenProvider;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserManagementService userManagementService;


    public AuthServiceImpl(AuthRepository authRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, UserManagementService userManagementService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userManagementService = userManagementService;
    }

    private AuthUser getByEmailOrThrowAuthUser(String email) {
        return authRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    private AuthUser getByIdOrThrowAuthUser(String id) {
        return authRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public LoginResponse login(LoginForm form) {
        AuthUser user = getByEmailOrThrowAuthUser(form.getEmail());
        User userDetails = userManagementService.getUserById(user.getId());

        if(userDetails.isBlocked()) {
            throw new ApplicationException(ErrorCode.USER_BLOCKED);
        }

        if(passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            String token = tokenProvider.generateToken(user.getId());
            return new LoginResponse(token);
        }
        throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
    }

    @Override
    public void createUserWithPassword(CreateUserWithPasswordForm form) {
        User user = userManagementService.save(form);

        AuthUser authUser = AuthUser.builder()
                .id(user.getId())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .build();

        authRepository.save(authUser);
    }

    @Override
    public void changePassword(ChangePasswordForm form) {
        AuthUser user = getByEmailOrThrowAuthUser(form.getEmail());

        if(passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
            authRepository.save(user);
        } else {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }
    }

    @Override
    public void logout(String userId) {
        // TODO implement if enough time for activity monitoring
    }
}
