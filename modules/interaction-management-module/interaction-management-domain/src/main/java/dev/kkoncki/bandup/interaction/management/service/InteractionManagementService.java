package dev.kkoncki.bandup.interaction.management.service;

import dev.kkoncki.bandup.interaction.management.Friendship;
import dev.kkoncki.bandup.interaction.management.forms.*;
import dev.kkoncki.bandup.user.management.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface InteractionManagementService {

    // Friendship
    void sendFriendRequest(String requesterId, String addresseeId);
    void respondToFriendRequest(@Valid RespondToFriendRequestForm form, String userId);
    List<User> getFriends(String userId);
    List<Friendship> getPendingFriendRequests(String userId);
    void removeFriendship(String friendshipId, String userId);

    // Follow
    void followUser(String followerId, String followedId);
    void unfollowUser(String followerId, String followedId);
    List<User> getFollowers(String userId);
    List<User> getFollowedUsers(String userId);

    // Block
    void blockUser(String blockerId, String blockedId);
    void unblockUser(String blockerId, String blockedId);
    List<User> getBlockedUsers(String userId);

    // Recommendation
    List<User> recommendUsers(String userId);
}
