package dev.kkoncki.bandup.interaction.management;

public class FriendshipMapper {

    public static Friendship toDomain(FriendshipEntity friendshipEntity) {
        return Friendship.builder()
                .id(friendshipEntity.getId())
                .requesterId(friendshipEntity.getRequesterId())
                .addresseeId(friendshipEntity.getAddresseeId())
                .status(friendshipEntity.getStatus())
                .timestamp(friendshipEntity.getTimestamp())
                .build();
    }

    public static FriendshipEntity toEntity(Friendship friendship) {
        return FriendshipEntity.builder()
                .id(friendship.getId())
                .requesterId(friendship.getRequesterId())
                .addresseeId(friendship.getAddresseeId())
                .status(friendship.getStatus())
                .timestamp(friendship.getTimestamp())
                .build();
    }
}
