package dev.kkoncki.bandup.interaction.management;

public class FollowMapper {

    public static Follow toDomain(FollowEntity entity) {
        return Follow.builder()
                .id(entity.getId())
                .followerId(entity.getFollowerId())
                .followedId(entity.getFollowedId())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public static FollowEntity toEntity(Follow follow) {
        return FollowEntity.builder()
                .id(follow.getId())
                .followerId(follow.getFollowerId())
                .followedId(follow.getFollowedId())
                .timestamp(follow.getTimestamp())
                .build();
    }
}
