package dev.kkoncki.bandup.interaction.management.repository;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.interaction.management.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InteractionManagementRepositoryAdapter implements InteractionManagementRepository {

    private final JpaFriendshipRepository jpaFriendshipRepository;
    private final JpaFollowRepository jpaFollowRepository;
    private final JpaBlockRepository jpaBlockRepository;

    public InteractionManagementRepositoryAdapter(JpaFriendshipRepository jpaFriendshipRepository, JpaFollowRepository jpaFollowRepository, JpaBlockRepository jpaBlockRepository) {
        this.jpaFriendshipRepository = jpaFriendshipRepository;
        this.jpaFollowRepository = jpaFollowRepository;
        this.jpaBlockRepository = jpaBlockRepository;
    }

    // Friendship

    @Override
    public void saveFriendship(Friendship friendship) {
        jpaFriendshipRepository.save(FriendshipMapper.toEntity(friendship));
    }

    @Override
    public void updateFriendshipStatus(String friendshipId, FriendshipStatus status) {
        FriendshipEntity friendship = jpaFriendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        friendship.setStatus(status);
        jpaFriendshipRepository.save(friendship);
    }

    @Override
    public void deleteFriendship(String friendshipId) {
        jpaFriendshipRepository.deleteById(friendshipId);
    }

    @Override
    public Optional<Friendship> findFriendshipByUser(String requesterId, String addresseeId) {
        return jpaFriendshipRepository.findByRequesterIdAndAddresseeId(requesterId, addresseeId)
                .map(FriendshipMapper::toDomain);
    }

    @Override
    public List<Friendship> findFriendshipsByUser(String userId) {
        return jpaFriendshipRepository.findByRequesterIdOrAddresseeId(userId, userId)
                .stream()
                .map(FriendshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsFriendship(String requesterId, String addresseeId) {
        return jpaFriendshipRepository.existsFriendship(requesterId, addresseeId);
    }

    @Override
    public void saveFollow(Follow follow) {
        jpaFollowRepository.save(FollowMapper.toEntity(follow));
    }

    @Override
    public void deleteFollow(String followerId, String followedId) {
        jpaFollowRepository.deleteByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public boolean isFollowing(String followerId, String followedId) {
        return jpaFollowRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public List<Follow> findFollowers(String userId) {
        return jpaFollowRepository.findByFollowedId(userId)
                .stream()
                .map(FollowMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Follow> findFollowed(String userId) {
        return jpaFollowRepository.findByFollowerId(userId)
                .stream()
                .map(FollowMapper::toDomain)
                .collect(Collectors.toList());
    }

    // Block

    @Override
    public void saveBlock(Block block) {
        jpaBlockRepository.save(BlockMapper.toEntity(block));
    }

    @Override
    public void deleteBlock(String blockerId, String blockedId) {
        jpaBlockRepository.deleteByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    @Override
    public List<Block> findBlocksByUser(String userId) {
        return jpaBlockRepository.findByBlockerId(userId)
                .stream()
                .map(BlockMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsBlock(String blockerId, String blockedId) {
        return jpaBlockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }
}
