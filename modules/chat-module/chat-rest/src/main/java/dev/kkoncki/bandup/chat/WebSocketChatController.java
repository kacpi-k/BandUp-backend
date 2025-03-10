package dev.kkoncki.bandup.chat;

import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import dev.kkoncki.bandup.chat.service.ChatService;
import dev.kkoncki.bandup.commons.LoggedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Slf4j
@Controller
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final LoggedUser loggedUser;

    public WebSocketChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService, LoggedUser loggedUser) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.loggedUser = loggedUser;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public GroupChatMessage sendMessage(SendGroupChatMessageForm form) {
        String senderId = loggedUser.getUserId();

        chatService.saveGroupMessage(form, senderId);

        GroupChatMessage message = GroupChatMessage.builder()
                .senderId(senderId)
                .bandId(form.getBandId())
                .content(form.getContent())
                .timestamp(Instant.now())
                .build();

        log.info("Received group message: {}", message);
        return message;
    }

    @MessageMapping("/send/private")
    public void sendPrivateMessage(SendPrivateChatMessageForm form) {
        String senderId = loggedUser.getUserId();

        chatService.savePrivateMessage(form, senderId);

        PrivateChatMessage message = PrivateChatMessage.builder()
                .senderId(senderId)
                .receiverId(form.getReceiverId())
                .content(form.getContent())
                .timestamp(Instant.now())
                .build();

        messagingTemplate.convertAndSend(
                "/queue/private-messages-user" + form.getReceiverId(),
                message
        );

        log.info("Private message sent: {}", message);
    }
}
