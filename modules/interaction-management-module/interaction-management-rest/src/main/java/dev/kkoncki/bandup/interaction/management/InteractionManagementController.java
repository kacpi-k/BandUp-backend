package dev.kkoncki.bandup.interaction.management;

import dev.kkoncki.bandup.commons.LoggedUser;
import dev.kkoncki.bandup.interaction.management.forms.*;
import dev.kkoncki.bandup.interaction.management.service.InteractionManagementService;
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
        interactionManagementService.sendFriendRequest(form); // TODO rethink if requesterId should be passed in the form or by the logged user
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
        interactionManagementService.followUser(form); // TODO same as sendFriendRequest
    }

    @DeleteMapping("/follow")
    public void unfollowUser(@RequestBody UnfollowUserForm form) {
        interactionManagementService.unfollowUser(form); // TODO same as above
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
        interactionManagementService.blockUser(form); // TODO same as above
    }

    @DeleteMapping("/block")
    public void unblockUser(@RequestBody UnblockUserForm form) {
        interactionManagementService.unblockUser(form); // TODO same as above
    }

    @GetMapping("/blocked")
    public List<Block> getBlockedUsers() {
        return interactionManagementService.getBlockedUsers(loggedUser.getUserId());
    }
}
