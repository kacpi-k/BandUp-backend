package dev.kkoncki.bandup.chat;

import dev.kkoncki.bandup.chat.service.ChatService;
import dev.kkoncki.bandup.commons.LoggedUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<PrivateChatMessage> getPrivateMessages(@PathVariable String receiverId) {
        String senderId = loggedUser.getUserId();

        return chatService.getPrivateMessages(senderId, receiverId);
    }

    @GetMapping("/group/{bandId}")
    public List<GroupChatMessage> getGroupMessages(@PathVariable String bandId) {
        return chatService.getGroupMessages(bandId);
    }
}