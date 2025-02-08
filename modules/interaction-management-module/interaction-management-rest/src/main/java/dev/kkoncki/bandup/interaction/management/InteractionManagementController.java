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

    @PostMapping("/friend/request")
    public void sendFriendRequest(@RequestBody SendFriendRequestForm form) {
        form.setRequesterId(loggedUser.getUserId());
        interactionManagementService.sendFriendRequest(form);
    }

    @PostMapping("/friend/respond")
    public void respondToFriendRequest(@RequestBody RespondToFriendRequestForm form) {
        interactionManagementService.respondToFriendRequest(form, loggedUser.getUserId());
    }

    @GetMapping("/friends")
    public List<Friendship> getFriends() {
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

    // Follow Endpoints

    @PostMapping("/follow")
    public void followUser(@RequestBody FollowUserForm form) {
        form.setFollowerId(loggedUser.getUserId());
        interactionManagementService.followUser(form);
    }

    @DeleteMapping("/follow")
    public void unfollowUser(@RequestBody UnfollowUserForm form) {
        form.setFollowerId(loggedUser.getUserId());
        interactionManagementService.unfollowUser(form);
    }

    @GetMapping("/followers")
    public List<Follow> getFollowers() {
        return interactionManagementService.getFollowers(loggedUser.getUserId());
    }

    @GetMapping("/followed")
    public List<Follow> getFollowed() {
        return interactionManagementService.getFollowedUsers(loggedUser.getUserId());
    }

    // Block Endpoints

    @PostMapping("/block")
    public void blockUser(@RequestBody BlockUserForm form) {
        form.setBlockerId(loggedUser.getUserId());
        interactionManagementService.blockUser(form);
    }

    @DeleteMapping("/block")
    public void unblockUser(@RequestBody UnblockUserForm form) {
        form.setBlockerId(loggedUser.getUserId());
        interactionManagementService.unblockUser(form);
    }

    @GetMapping("/blocked")
    public List<Block> getBlockedUsers() {
        return interactionManagementService.getBlockedUsers(loggedUser.getUserId());
    }

    // Recommendation Endpoints

    @GetMapping("/recommendations")
    public List<User> recommendUsers() {
        return interactionManagementService.recommendUsers(loggedUser.getUserId());
    }
}
