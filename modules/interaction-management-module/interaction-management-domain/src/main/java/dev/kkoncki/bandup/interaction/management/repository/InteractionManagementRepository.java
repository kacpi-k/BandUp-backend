package dev.kkoncki.bandup.interaction.management.repository;

import dev.kkoncki.bandup.interaction.management.Block;
import dev.kkoncki.bandup.interaction.management.Follow;
import dev.kkoncki.bandup.interaction.management.Friendship;
import dev.kkoncki.bandup.interaction.management.FriendshipStatus;

import java.util.List;
import java.util.Optional;

public interface InteractionManagementRepository {

    // Friendship
    void saveFriendship(Friendship friendship);
    void updateFriendshipStatus(String friendshipId, FriendshipStatus status);
    void deleteFriendship(String friendshipId);
    Optional<Friendship> findFriendshipByUser(String requesterId, String addresseeId);
    List<Friendship> findFriendshipsByUser(String userId);
    boolean existsFriendship(String requesterId, String addresseeId);

    // Follow
    void saveFollow(Follow follow);
    void deleteFollow(String followerId, String followedId);
    boolean isFollowing(String followerId, String followedId);
    List<Follow> findFollowers(String userId);
    List<Follow> findFollowed(String userId);

    // Block
    void saveBlock(Block block);
    void deleteBlock(String blockerId, String blockedId);
    List<Block> findBlocksByUser(String userId);
    boolean existsBlock(String blockerId, String blockedId);
}
