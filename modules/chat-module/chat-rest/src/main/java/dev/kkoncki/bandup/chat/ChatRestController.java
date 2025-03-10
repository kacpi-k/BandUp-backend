package dev.kkoncki.bandup.chat;

import dev.kkoncki.bandup.chat.service.ChatService;
import dev.kkoncki.bandup.commons.LoggedUser;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatService chatService;
    private final LoggedUser loggedUser;

    public ChatRestController(ChatService chatService, LoggedUser loggedUser) {
        this.chatService = chatService;
        this.loggedUser = loggedUser;
    }

    @GetMapping("/private/{receiverId}")
    public Page<PrivateChatMessage> getPrivateMessages(
            @PathVariable String receiverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        String senderId = loggedUser.getUserId();
        return chatService.getPrivateMessages(senderId, receiverId, page, size);
    }

    @PatchMapping("/private/read/{messageId}")
    public void markMessageAsRead(@PathVariable String messageId) {
        chatService.markMessageAsRead(messageId);
    }

    @GetMapping("/group/{bandId}")
    public Page<GroupChatMessage> getGroupMessages(
            @PathVariable String bandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return chatService.getGroupMessages(bandId, page, size);
    }
}