package dev.kkoncki.bandup.interaction.management;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.search.SearchForm;
import dev.kkoncki.bandup.commons.search.SearchResponse;
import dev.kkoncki.bandup.interaction.management.forms.*;
import dev.kkoncki.bandup.interaction.management.repository.InteractionManagementRepository;
import dev.kkoncki.bandup.interaction.management.service.InteractionManagementServiceImpl;
import dev.kkoncki.bandup.user.management.User;
import dev.kkoncki.bandup.user.management.service.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteractionManagementServiceTest {

    @Mock
    private InteractionManagementRepository repository;

    @Mock
    private UserManagementService userManagementService;

    @InjectMocks
    private InteractionManagementServiceImpl interactionService;

    private SendFriendRequestForm sendFriendRequestForm;
    private RespondToFriendRequestForm respondToFriendRequestForm;
    private FollowUserForm followUserForm;
    private UnfollowUserForm unfollowUserForm;
    private BlockUserForm blockUserForm;
    private UnblockUserForm unblockUserForm;

    @BeforeEach
    void setUp() {
        sendFriendRequestForm = new SendFriendRequestForm("user1", "user2");
        respondToFriendRequestForm = new RespondToFriendRequestForm("friendship1", FriendshipStatus.ACCEPTED);
        followUserForm = new FollowUserForm("user1", "user2");
        unfollowUserForm = new UnfollowUserForm("user1", "user2");
        blockUserForm = new BlockUserForm("user1", "user2");
        unblockUserForm = new UnblockUserForm("user1", "user2");
    }

    // Friendship Tests

    @Test
    void shouldSendFriendRequest() {
        when(repository.existsFriendship("user1", "user2")).thenReturn(false);

        interactionService.sendFriendRequest(sendFriendRequestForm);

        verify(repository, times(1)).saveFriendship(any(Friendship.class));
    }

    @Test
    void shouldNotSendFriendRequestIfAlreadyExists() {
        when(repository.existsFriendship("user1", "user2")).thenReturn(true);

        assertThrows(ApplicationException.class, () -> interactionService.sendFriendRequest(sendFriendRequestForm));
    }

    @Test
    void shouldRespondToFriendRequest() {
        Friendship friendship = new Friendship("friendship1", "user1", "user2", FriendshipStatus.PENDING, Instant.now());
        when(repository.findFriendshipByUser("friendship1", "user2")).thenReturn(Optional.of(friendship));

        interactionService.respondToFriendRequest(respondToFriendRequestForm, "user2");

        verify(repository, times(1)).updateFriendshipStatus("friendship1", FriendshipStatus.ACCEPTED);
    }

    @Test
    void shouldThrowExceptionWhenRespondingToNonexistentFriendRequest() {
        when(repository.findFriendshipByUser("friendship1", "user2")).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> interactionService.respondToFriendRequest(respondToFriendRequestForm, "user2"));
    }

    @Test
    void shouldGetFriends() {
        List<Friendship> friendships = List.of(new Friendship("1", "user1", "user2", FriendshipStatus.ACCEPTED, Instant.now()));
        when(repository.findFriendshipsByUser("user1")).thenReturn(friendships);

        List<Friendship> friends = interactionService.getFriends("user1");

        assertEquals(1, friends.size());
    }

    @Test
    void shouldGetPendingFriendRequests() {
        List<Friendship> friendships = List.of(new Friendship("1", "user1", "user2", FriendshipStatus.PENDING, Instant.now()));
        when(repository.findFriendshipsByUser("user1")).thenReturn(friendships);

        List<Friendship> requests = interactionService.getPendingFriendRequests("user1");

        assertEquals(1, requests.size());
    }

    @Test
    void shouldRemoveFriendship() {
        Friendship friendship = new Friendship("friendship1", "user1", "user2", FriendshipStatus.ACCEPTED, Instant.now());
        when(repository.findFriendshipByUser("friendship1", "user1")).thenReturn(Optional.of(friendship));

        interactionService.removeFriendship("friendship1", "user1");

        verify(repository, times(1)).deleteFriendship("friendship1");
    }

    // Follow Tests

    @Test
    void shouldFollowUser() {
        when(repository.isFollowing("user1", "user2")).thenReturn(false);

        interactionService.followUser(followUserForm);

        verify(repository, times(1)).saveFollow(any(Follow.class));
    }

    @Test
    void shouldNotFollowUserIfAlreadyFollowing() {
        when(repository.isFollowing("user1", "user2")).thenReturn(true);

        assertThrows(ApplicationException.class, () -> interactionService.followUser(followUserForm));
    }

    @Test
    void shouldUnfollowUser() {
        when(repository.isFollowing("user1", "user2")).thenReturn(true);

        interactionService.unfollowUser(unfollowUserForm);

        verify(repository, times(1)).deleteFollow("user1", "user2");
    }

    @Test
    void shouldThrowExceptionIfNotFollowing() {
        when(repository.isFollowing("user1", "user2")).thenReturn(false);

        assertThrows(ApplicationException.class, () -> interactionService.unfollowUser(unfollowUserForm));
    }

    @Test
    void shouldGetFollowers() {
        List<Follow> followers = List.of(new Follow("1", "user1", "user2", Instant.now()));
        when(repository.findFollowers("user1")).thenReturn(followers);

        List<Follow> result = interactionService.getFollowers("user1");

        assertEquals(1, result.size());
    }

    @Test
    void shouldGetFollowedUsers() {
        List<Follow> followed = List.of(new Follow("1", "user1", "user2", Instant.now()));
        when(repository.findFollowed("user1")).thenReturn(followed);

        List<Follow> result = interactionService.getFollowedUsers("user1");

        assertEquals(1, result.size());
    }

    // Block Tests

    @Test
    void shouldBlockUser() {
        when(repository.existsBlock("user1", "user2")).thenReturn(false);

        interactionService.blockUser(blockUserForm);

        verify(repository, times(1)).saveBlock(any(Block.class));
    }

    @Test
    void shouldNotBlockUserIfAlreadyBlocked() {
        when(repository.existsBlock("user1", "user2")).thenReturn(true);

        assertThrows(ApplicationException.class, () -> interactionService.blockUser(blockUserForm));
    }

    @Test
    void shouldUnblockUser() {
        when(repository.existsBlock("user1", "user2")).thenReturn(true);

        interactionService.unblockUser(unblockUserForm);

        verify(repository, times(1)).deleteBlock("user1", "user2");
    }

    @Test
    void shouldThrowExceptionIfUserNotBlocked() {
        when(repository.existsBlock("user1", "user2")).thenReturn(false);

        assertThrows(ApplicationException.class, () -> interactionService.unblockUser(unblockUserForm));
    }

    @Test
    void shouldGetBlockedUsers() {
        List<Block> blocks = List.of(new Block("1", "user1", "user2", Instant.now()));
        when(repository.findBlocksByUser("user1")).thenReturn(blocks);

        List<Block> result = interactionService.getBlockedUsers("user1");

        assertEquals(1, result.size());
    }

    @Test
    void shouldRecommendUsersBasedOnLocationAndGenres() {
        User currentUser = User.builder()
                .id("user1")
                .latitude(52.2298)
                .longitude(21.0122)
                .genres(List.of("rock", "jazz"))
                .build();

        User similarUser1 = User.builder()
                .id("user2")
                .latitude(52.2299)
                .longitude(21.0125)
                .genres(List.of("rock", "pop"))
                .build();

        User similarUser2 = User.builder()
                .id("user3")
                .latitude(52.2300)
                .longitude(21.0130)
                .genres(List.of("jazz", "blues"))
                .build();

        User distantUser = User.builder()
                .id("user4")
                .latitude(40.7128) // New York
                .longitude(-74.0060)
                .genres(List.of("metal", "hip-hop"))
                .build();

        SearchResponse<User> searchResponse = new SearchResponse<>(
                List.of(similarUser1, similarUser2, distantUser),
                3L
        );

        when(userManagementService.get("user1")).thenReturn(currentUser);
        when(userManagementService.search(any(SearchForm.class))).thenReturn(searchResponse);

        List<User> recommendations = interactionService.recommendUsers("user1");

        assertEquals(2, recommendations.size());
        assertTrue(recommendations.contains(similarUser1));
        assertTrue(recommendations.contains(similarUser2));
        assertFalse(recommendations.contains(distantUser));

        verify(userManagementService, times(1)).get("user1");
        verify(userManagementService, times(1)).search(any(SearchForm.class));
    }
}

