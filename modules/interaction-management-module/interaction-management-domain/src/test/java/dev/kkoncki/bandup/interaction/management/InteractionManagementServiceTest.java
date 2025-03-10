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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private RespondToFriendRequestForm respondToFriendRequestForm;
    private String requesterId;
    private String addresseeId;
    private String followerId;
    private String followedId;
    private String blockerId;
    private String blockedId;

    @BeforeEach
    void setUp() {
        respondToFriendRequestForm = new RespondToFriendRequestForm("friendship1", FriendshipStatus.ACCEPTED);
        requesterId = "user1";
        addresseeId = "user2";
        followerId = "user1";
        followedId = "user2";
        blockerId = "user1";
        blockedId = "user2";
    }

    // Friendship Tests

    @Test
    void shouldSendFriendRequest() {
        when(repository.existsFriendship(requesterId, addresseeId)).thenReturn(false);

        interactionService.sendFriendRequest(requesterId, addresseeId);

        verify(repository, times(1)).saveFriendship(any(Friendship.class));
    }


    @Test
    void shouldNotSendFriendRequestIfAlreadyExists() {
        when(repository.existsFriendship("user1", "user2")).thenReturn(true);

        assertThrows(ApplicationException.class, () -> interactionService.sendFriendRequest(requesterId, addresseeId));
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
        List<Friendship> friendships = List.of(
                new Friendship("1", requesterId, addresseeId, FriendshipStatus.ACCEPTED, Instant.now())
        );
        when(repository.findFriendshipsByUser(requesterId)).thenReturn(friendships);

        List<User> mockUsers = List.of(
                new User(addresseeId, "John", "Doe", "email@test.pl", Instant.now(), false, new ArrayList<>(), "bio", new ArrayList<>(),"image", 52.2298, 21.0122, "Warsaw", "Poland")
        );
        when(userManagementService.getAllByIds(List.of(addresseeId))).thenReturn(mockUsers);

        List<User> friends = interactionService.getFriends(requesterId);

        assertEquals(1, friends.size());
        assertEquals("John", friends.get(0).getFirstName());
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

        interactionService.followUser(followerId, followedId);

        verify(repository, times(1)).saveFollow(any(Follow.class));
    }

    @Test
    void shouldNotFollowUserIfAlreadyFollowing() {
        when(repository.isFollowing("user1", "user2")).thenReturn(true);

        assertThrows(ApplicationException.class, () -> interactionService.followUser(followerId, followedId));
    }

    @Test
    void shouldUnfollowUser() {
        when(repository.isFollowing("user1", "user2")).thenReturn(true);

        interactionService.unfollowUser(followerId, followedId);

        verify(repository, times(1)).deleteFollow("user1", "user2");
    }

    @Test
    void shouldThrowExceptionIfNotFollowing() {
        when(repository.isFollowing("user1", "user2")).thenReturn(false);

        assertThrows(ApplicationException.class, () -> interactionService.unfollowUser(followerId, followedId));
    }

    @Test
    void shouldGetFollowers() {
        List<Follow> followers = List.of(
                new Follow("1", followerId, followedId, Instant.now())
        );
        when(repository.findFollowers(followedId)).thenReturn(followers);

        List<User> mockUsers = List.of(
                new User(followerId, "John", "Doe", "email@test.pl", Instant.now(), false, new ArrayList<>(), "bio", new ArrayList<>(),"image", 52.2298, 21.0122, "Warsaw", "Poland")
        );
        when(userManagementService.getAllByIds(List.of(followerId))).thenReturn(mockUsers);

        List<User> result = interactionService.getFollowers(followedId);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }


    @Test
    void shouldGetFollowedUsers() {
        List<Follow> followed = List.of(
                new Follow("1", followerId, followedId, Instant.now())
        );
        when(repository.findFollowed(followerId)).thenReturn(followed);

        List<User> mockUsers = List.of(
                new User(followedId, "John", "Doe", "email@test.pl", Instant.now(), false, new ArrayList<>(), "bio", new ArrayList<>(),"image", 52.2298, 21.0122, "Warsaw", "Poland")
        );
        when(userManagementService.getAllByIds(List.of(followedId))).thenReturn(mockUsers);

        List<User> result = interactionService.getFollowedUsers(followerId);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }


    // Block Tests

    @Test
    void shouldBlockUser() {
        when(repository.existsBlock("user1", "user2")).thenReturn(false);

        interactionService.blockUser(blockerId, blockedId);

        verify(repository, times(1)).saveBlock(any(Block.class));
    }

    @Test
    void shouldNotBlockUserIfAlreadyBlocked() {
        when(repository.existsBlock("user1", "user2")).thenReturn(true);

        assertThrows(ApplicationException.class, () -> interactionService.blockUser(blockerId, blockedId));
    }

    @Test
    void shouldUnblockUser() {
        when(repository.existsBlock("user1", "user2")).thenReturn(true);

        interactionService.unblockUser(blockerId, blockedId);

        verify(repository, times(1)).deleteBlock("user1", "user2");
    }

    @Test
    void shouldThrowExceptionIfUserNotBlocked() {
        when(repository.existsBlock("user1", "user2")).thenReturn(false);

        assertThrows(ApplicationException.class, () -> interactionService.unblockUser(blockerId, blockedId));
    }

    @Test
    void shouldGetBlockedUsers() {
        List<Block> blocks = List.of(
                new Block("1", blockerId, blockedId, Instant.now())
        );
        when(repository.findBlocksByUser(blockerId)).thenReturn(blocks);

        List<User> mockUsers = List.of(
                new User(blockedId, "John", "Doe", "email@test.pl", Instant.now(), false, new ArrayList<>(), "bio", new ArrayList<>(),"image", 52.2298, 21.0122, "Warsaw", "Poland")
        );
        when(userManagementService.getAllByIds(List.of(blockedId))).thenReturn(mockUsers);

        List<User> result = interactionService.getBlockedUsers(blockerId);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }


    @Test
    void shouldRecommendUsersBasedOnLocationGenresAndInstruments() {
        User currentUser = User.builder()
                .id("user1")
                .genres(List.of("rock", "pop"))
                .instruments(List.of("guitar", "drums"))
                .city("Warsaw")
                .country("Poland")
                .latitude(52.2298)
                .longitude(21.0122)
                .build();

        User user2 = User.builder()
                .id("user2")
                .genres(List.of("rock", "pop"))
                .instruments(List.of("guitar", "bass"))
                .city("Warsaw")
                .country("Poland")
                .latitude(52.2298)
                .longitude(21.0122)
                .build();

        User user3 = User.builder()
                .id("user3")
                .genres(List.of("jazz", "blues"))
                .instruments(List.of("piano", "drums"))
                .city("Warsaw")
                .country("Poland")
                .latitude(52.2298)
                .longitude(21.0122)
                .build();

        User user4 = User.builder()
                .id("user4")
                .genres(List.of("metal", "hip-hop"))
                .instruments(List.of("violin"))
                .city("Gdansk")
                .country("Poland")
                .latitude(54.3520)
                .longitude(18.6466)
                .build();

        List<User> allUsers = List.of(currentUser, user2, user3, user4);

        when(userManagementService.get("user1")).thenReturn(currentUser);
        when(userManagementService.search(any(SearchForm.class)))
                .thenReturn(new SearchResponse<>(allUsers, (long) allUsers.size()));

        List<User> recommendedUsers = interactionService.recommendUsers("user1");

        System.out.println("Zarekomendowani użytkownicy: " + recommendedUsers.stream()
                .map(User::getId)
                .collect(Collectors.joining(", ")));

        assertEquals(2, recommendedUsers.size());
        assertTrue(recommendedUsers.contains(user2));
        assertTrue(recommendedUsers.contains(user3));
        assertFalse(recommendedUsers.contains(user4));
    }
}