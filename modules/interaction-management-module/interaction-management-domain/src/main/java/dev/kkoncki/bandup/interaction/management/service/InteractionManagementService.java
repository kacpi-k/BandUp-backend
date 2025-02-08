package dev.kkoncki.bandup.interaction.management.service;

import dev.kkoncki.bandup.interaction.management.Block;
import dev.kkoncki.bandup.interaction.management.Follow;
import dev.kkoncki.bandup.interaction.management.Friendship;
import dev.kkoncki.bandup.interaction.management.forms.*;
import dev.kkoncki.bandup.user.management.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface InteractionManagementService {

    // Friendship
    void sendFriendRequest(@Valid SendFriendRequestForm form);
    void respondToFriendRequest(@Valid RespondToFriendRequestForm form, String userId);
    List<Friendship> getFriends(String userId);
    List<Friendship> getPendingFriendRequests(String userId);
    void removeFriendship(String friendshipId, String userId);

    // Follow
    void followUser(@Valid FollowUserForm form);
    void unfollowUser(@Valid UnfollowUserForm form);
    List<Follow> getFollowers(String userId);
    List<Follow> getFollowedUsers(String userId);

    // Block
    void blockUser(@Valid BlockUserForm form);
    void unblockUser(@Valid UnblockUserForm form);
    List<Block> getBlockedUsers(String userId);

    // Recommendation
    List<User> recommendUsers(String userId);
}
