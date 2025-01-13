package dev.kkoncki.bandup.user.management;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .createdOn(entity.getCreatedOn())
                .isBlocked(entity.isBlocked())
                .bio(entity.getBio())
                .build();
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdOn(user.getCreatedOn())
                .isBlocked(user.isBlocked())
                .bio(user.getBio())
                .build();
    }
}
