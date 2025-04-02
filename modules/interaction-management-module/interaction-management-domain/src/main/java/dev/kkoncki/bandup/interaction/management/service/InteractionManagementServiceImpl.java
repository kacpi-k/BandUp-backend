package dev.kkoncki.bandup.interaction.management.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.interaction.management.Block;
import dev.kkoncki.bandup.interaction.management.Follow;
import dev.kkoncki.bandup.interaction.management.Friendship;
import dev.kkoncki.bandup.interaction.management.FriendshipStatus;
import dev.kkoncki.bandup.interaction.management.forms.*;
import dev.kkoncki.bandup.interaction.management.repository.InteractionManagementRepository;
import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.service.UserManagementService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class InteractionManagementServiceImpl implements InteractionManagementService {

    private final InteractionManagementRepository repository;
    private final UserManagementService userManagementService;

    public InteractionManagementServiceImpl(InteractionManagementRepository repository, UserManagementService userManagementService) {
        this.repository = repository;
        this.userManagementService = userManagementService;
    }

    private Friendship getFriendshipOrThrow(String friendshipId, String userId) {
        return repository.findFriendshipByUser(friendshipId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.FRIENDSHIP_NOT_FOUND));
    }

    // Friendship

    @Override
    public void sendFriendRequest(String requesterId, String addresseeId) {
        if (repository.existsFriendship(requesterId, addresseeId)) {
            throw new ApplicationException(ErrorCode.FRIENDSHIP_ALREADY_EXISTS);
        }

        Friendship friendship = Friendship.builder()
                .id(UUID.randomUUID().toString())
                .requesterId(requesterId)
                .addresseeId(addresseeId)
                .status(FriendshipStatus.ACCEPTED)
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
    public List<User> getFriends(String userId) {
        List<Friendship> friendships = repository.findFriendshipsByUser(userId).stream()
                .filter(friendship -> friendship.getStatus() == FriendshipStatus.ACCEPTED)
                .toList();

        List<String> friendIds = friendships.stream()
                .map(friendship -> friendship.getRequesterId().equals(userId) ? friendship.getAddresseeId() : friendship.getRequesterId())
                .toList();

        return userManagementService.getAllByIds(friendIds);
    }

    @Override
    public List<Friendship> getPendingFriendRequests(String userId) {
        return repository.findFriendshipsByUser(userId).stream()
                .filter(friendship -> friendship.getStatus() == FriendshipStatus.PENDING)
                .toList();
    }

    @Override
    public void removeFriendship(String friendshipId, String userId) {
//        Friendship friendship = getFriendshipOrThrow(friendshipId, userId);
//
//        if(!friendship.getRequesterId().equals(userId) && !friendship.getAddresseeId().equals(userId)) {
//            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
//        }

        repository.deleteFriendship(friendshipId);
    }

    @Override
    public List<Friendship> getFriendshipWithUser(String loggedUser, String userId) {
        return repository.findFriendshipWithUser(loggedUser, userId);
    }

    // Follow

    @Override
    public void followUser(String followerId, String followedId) {
        if (repository.isFollowing(followerId, followedId)) {
            throw new ApplicationException(ErrorCode.ALREADY_FOLLOWING);
        }

        Follow follow = Follow.builder()
                .id(UUID.randomUUID().toString())
                .followerId(followerId)
                .followedId(followedId)
                .timestamp(Instant.now())
                .build();

        repository.saveFollow(follow);
    }

    @Override
    public void unfollowUser(String followerId, String followedId) {
        if (!repository.isFollowing(followerId, followedId)) {
            throw new ApplicationException(ErrorCode.NOT_FOLLOWING);
        }

        repository.deleteFollow(followerId, followedId);
    }

    @Override
    public List<User> getFollowers(String userId) {
        List<Follow> followers = repository.findFollowers(userId);

        List<String> followerIds = followers.stream()
                .map(Follow::getFollowerId)
                .toList();

        return userManagementService.getAllByIds(followerIds);
    }

    @Override
    public List<User> getFollowedUsers(String userId) {
        List<Follow> followed = repository.findFollowed(userId);

        List<String> followedIds = followed.stream()
                .map(Follow::getFollowedId)
                .toList();

        return userManagementService.getAllByIds(followedIds);
    }

    // Block

    @Override
    public void blockUser(String blockerId, String blockedId) {
        if (repository.existsBlock(blockerId, blockedId)) {
            throw new ApplicationException(ErrorCode.USER_ALREADY_BLOCKED);
        }

        Block block = Block.builder()
                .id(UUID.randomUUID().toString())
                .blockerId(blockerId)
                .blockedId(blockedId)
                .timestamp(Instant.now())
                .build();

        repository.saveBlock(block);
    }

    @Override
    public void unblockUser(String blockerId, String blockedId) {
        if (!repository.existsBlock(blockerId, blockedId)) {
            throw new ApplicationException(ErrorCode.USER_NOT_BLOCKED);
        }

        repository.deleteBlock(blockerId, blockedId);
    }

    @Override
    public List<User> getBlockedUsers(String userId) {
        List<Block> blocked = repository.findBlocksByUser(userId);

        List<String> blockedIds = blocked.stream()
                .map(Block::getBlockedId)
                .toList();

        return userManagementService.getAllByIds(blockedIds);
    }

    // Recommendation

    @Override
    public List<User> recommendUsers(String userId) {
        int minScore = 45;
        User currentUser = userManagementService.get(userId);

//        SearchForm searchForm = new SearchForm();
//        searchForm.setPage(1);
//        searchForm.setSize(100);
        List<User> allUsers = userManagementService.getAll();

        List<String> friends = repository.findFriendshipsByUser(userId).stream()
                .map(friendship -> friendship.getRequesterId().equals(userId) ? friendship.getAddresseeId() : friendship.getRequesterId())
                .toList();

        List<String> blockedUsers = repository.findBlocksByUser(userId).stream()
                .map(Block::getBlockedId)
                .toList();

        return allUsers.stream()
                .filter(user -> !user.getId().equals(userId)) // exclude ourselves
                .filter(user -> !friends.contains(user.getId())) // exclude friends
                .filter(user -> !blockedUsers.contains(user.getId())) // exclude blocked users
                .filter(user -> isWithinMaxDistance(currentUser, user, 100)) // filter by distance
                .map(user -> new AbstractMap.SimpleEntry<>(user, calculateMatchScore(currentUser, user)))
                .filter(entry -> entry.getValue() >= minScore)
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
                .limit(20)
                .map(AbstractMap.SimpleEntry::getKey)
                .toList();
    }

    private int calculateMatchScore(User currentUser, User otherUser) {
        int score = 0;

        long commonGenres = currentUser.getGenres().stream()
                .filter(otherUser.getGenres()::contains)
                .count();
        score += (int) (commonGenres * 10);

        long commonInstruments = currentUser.getInstruments().stream()
                .filter(otherUser.getInstruments()::contains)
                .count();
        score += (int) (commonInstruments * 15);

        if (Objects.equals(currentUser.getCity(), otherUser.getCity())) {
            score += 30;
        } else if (Objects.equals(currentUser.getCountry(), otherUser.getCountry())) {
            score += 10;
        }

        return score;
    }

    private boolean isWithinMaxDistance(User currentUser, User otherUser, double maxDistanceKm) {
        if (currentUser.getLatitude() == null || currentUser.getLongitude() == null ||
                otherUser.getLatitude() == null || otherUser.getLongitude() == null) {
            return false;
        }

        double distance = haversine(
                currentUser.getLatitude(), currentUser.getLongitude(),
                otherUser.getLatitude(), otherUser.getLongitude()
        );

        return distance <= maxDistanceKm;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
