package dev.kkoncki.bandup.interaction.management.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.interaction.management.Block;
import dev.kkoncki.bandup.interaction.management.Follow;
import dev.kkoncki.bandup.interaction.management.Friendship;
import dev.kkoncki.bandup.interaction.management.FriendshipStatus;
import dev.kkoncki.bandup.interaction.management.forms.*;
import dev.kkoncki.bandup.interaction.management.repository.InteractionManagementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class InteractionManagementServiceImpl implements InteractionManagementService {

    private final InteractionManagementRepository repository;

    public InteractionManagementServiceImpl(InteractionManagementRepository repository) {
        this.repository = repository;
    }

    private Friendship getFriendshipOrThrow(String friendshipId, String userId) {
        return repository.findFriendshipByUser(friendshipId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.FRIENDSHIP_NOT_FOUND));
    }

    // Friendship

    @Override
    public void sendFriendRequest(SendFriendRequestForm form) {
        if (repository.existsFriendship(form.getRequesterId(), form.getAddresseeId())) {
            throw new ApplicationException(ErrorCode.FRIENDSHIP_ALREADY_EXISTS);
        }

        Friendship friendship = Friendship.builder()
                .id(UUID.randomUUID().toString())
                .requesterId(form.getRequesterId())
                .addresseeId(form.getAddresseeId())
                .status(FriendshipStatus.PENDING)
                .timestamp(Instant.now())
                .build();

        repository.saveFriendship(friendship);
    }

    @Override
    public void respondToFriendRequest(RespondToFriendRequestForm form, String userId) {
        Friendship friendship = getFriendshipOrThrow(form.getFriendshipId(), userId);

        if(!friendship.getAddresseeId().equals(userId)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        repository.updateFriendshipStatus(form.getFriendshipId(), form.getStatus());
    }

    @Override
    public List<Friendship> getFriends(String userId) {
        return repository.findFriendshipsByUser(userId).stream()
                .filter(friendship -> friendship.getStatus() == FriendshipStatus.ACCEPTED)
                .toList();
    }

    @Override
    public List<Friendship> getPendingFriendRequests(String userId) {
        return repository.findFriendshipsByUser(userId).stream()
                .filter(friendship -> friendship.getStatus() == FriendshipStatus.PENDING)
                .toList();
    }

    @Override
    public void removeFriendship(String friendshipId, String userId) {
        Friendship friendship = getFriendshipOrThrow(friendshipId, userId);

        if(!friendship.getRequesterId().equals(userId) || !friendship.getAddresseeId().equals(userId)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        repository.deleteFriendship(friendshipId);
    }

    // Follow

    @Override
    public void followUser(FollowUserForm form) {
        if (repository.isFollowing(form.getFollowerId(), form.getFollowedId())) {
            throw new ApplicationException(ErrorCode.ALREADY_FOLLOWING);
        }

        Follow follow = Follow.builder()
                .id(UUID.randomUUID().toString())
                .followerId(form.getFollowerId())
                .followedId(form.getFollowedId())
                .timestamp(Instant.now())
                .build();

        repository.saveFollow(follow);
    }

    @Override
    public void unfollowUser(UnfollowUserForm form) {
        if (!repository.isFollowing(form.getFollowerId(), form.getFollowedId())) {
            throw new ApplicationException(ErrorCode.NOT_FOLLOWING);
        }

        repository.deleteFollow(form.getFollowerId(), form.getFollowedId());
    }

    @Override
    public List<Follow> getFollowers(String userId) {
        return repository.findFollowers(userId);
    }

    @Override
    public List<Follow> getFollowedUsers(String userId) {
        return repository.findFollowed(userId);
    }

    // Block

    @Override
    public void blockUser(BlockUserForm form) {
        if (repository.existsBlock(form.getBlockerId(), form.getBlockedId())) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_BLOCKED);
        }

        Block block = Block.builder()
                .id(UUID.randomUUID().toString())
                .blockerId(form.getBlockerId())
                .blockedId(form.getBlockedId())
                .timestamp(Instant.now())
                .build();

        repository.saveBlock(block);
    }

    @Override
    public void unblockUser(UnblockUserForm form) {
        if (!repository.isBlocked(form.getBlockerId(), form.getBlockedId())) {
            throw new ApplicationException(ErrorCode.USER_NOT_BLOCKED);
        }

        repository.deleteBlock(form.getBlockerId(), form.getBlockedId());
    }

    @Override
    public List<Block> getBlockedUsers(String userId) {
        return repository.findBlocksByUser(userId);
    }
}
