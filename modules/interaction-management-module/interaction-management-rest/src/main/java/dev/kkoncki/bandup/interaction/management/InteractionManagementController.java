package dev.kkoncki.bandup.interaction.management;

import dev.kkoncki.bandup.commons.LoggedUser;
import dev.kkoncki.bandup.interaction.management.forms.*;
import dev.kkoncki.bandup.interaction.management.service.InteractionManagementService;
import dev.kkoncki.bandup.user.management.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interaction-management")
public class InteractionManagementController {

    private final InteractionManagementService interactionManagementService;
    private final LoggedUser loggedUser;

    public InteractionManagementController(InteractionManagementService interactionManagementService, LoggedUser loggedUser) {
        this.interactionManagementService = interactionManagementService;
        this.loggedUser = loggedUser;
    }

    // Friendship Endpoints

    @PostMapping("/friend/request/{addresseeId}")
    public void sendFriendRequest(@PathVariable String addresseeId) {
        interactionManagementService.sendFriendRequest(loggedUser.getUserId(), addresseeId);
    }

    @PostMapping("/friend/respond")
    public void respondToFriendRequest(@RequestBody RespondToFriendRequestForm form) {
        interactionManagementService.respondToFriendRequest(form, loggedUser.getUserId());
    }

    @GetMapping("/friends")
    public List<User> getFriends() {
        return interactionManagementService.getFriends(loggedUser.getUserId());
    }

    @GetMapping("/friend/pending")
    public List<Friendship> getPendingFriendRequests() {
        return interactionManagementService.getPendingFriendRequests(loggedUser.getUserId());
    }

    @DeleteMapping("/friend/{friendshipId}")
    public void deleteFriendship(@PathVariable String friendshipId) {
        interactionManagementService.removeFriendship(friendshipId, loggedUser.getUserId());
    }

    @GetMapping("friend/with/{userId}")
    public List<Friendship> getFriendshipWithUser(@PathVariable String userId) {
        return interactionManagementService.getFriendshipWithUser(loggedUser.getUserId(), userId);
    }

    // Follow Endpoints

    @PostMapping("/follow/{followedId}")
    public void followUser(@PathVariable String followedId) {
        interactionManagementService.followUser(loggedUser.getUserId(), followedId);
    }

    @DeleteMapping("/unfollow/{followedId}")
    public void unfollowUser(@PathVariable String followedId) {
        interactionManagementService.unfollowUser(loggedUser.getUserId(), followedId);
    }

    @GetMapping("/followers")
    public List<User> getFollowers() {
        return interactionManagementService.getFollowers(loggedUser.getUserId());
    }

    @GetMapping("/followed")
    public List<User> getFollowed() {
        return interactionManagementService.getFollowedUsers(loggedUser.getUserId());
    }

    // Block Endpoints

    @PostMapping("/block/{blockedId}")
    public void blockUser(@PathVariable String blockedId) {
        interactionManagementService.blockUser(loggedUser.getUserId(), blockedId);
    }

    @DeleteMapping("/unblock/{blockedId}")
    public void unblockUser(@PathVariable String blockedId) {
        interactionManagementService.unblockUser(loggedUser.getUserId(), blockedId);
    }

    @GetMapping("/blocked")
    public List<User> getBlockedUsers() {
        return interactionManagementService.getBlockedUsers(loggedUser.getUserId());
    }

    // Recommendation Endpoints

    @GetMapping("/recommendations")
    public List<User> recommendUsers() {
        return interactionManagementService.recommendUsers(loggedUser.getUserId());
    }
}
