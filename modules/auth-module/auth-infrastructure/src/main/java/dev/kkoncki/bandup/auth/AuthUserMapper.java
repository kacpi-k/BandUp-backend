package dev.kkoncki.bandup.auth;

public class AuthUserMapper {

    public static AuthUser toAuthUser(AuthUserEntity authUserEntity) {
        return AuthUser.builder()
                .id(authUserEntity.getId())
                .email(authUserEntity.getEmail())
                .password(authUserEntity.getPassword())
                .build();
    }

    public static AuthUserEntity toAuthUserEntity(AuthUser authUser) {
        return AuthUserEntity.builder()
                .id(authUser.getId())
                .email(authUser.getEmail())
                .password(authUser.getPassword())
                .build();
    }
}
