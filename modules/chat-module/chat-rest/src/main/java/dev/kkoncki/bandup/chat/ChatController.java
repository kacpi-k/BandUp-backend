package dev.kkoncki.bandup.chat;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.commons.LoggedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;

@Slf4j
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public GroupChatMessage sendMessage(GroupChatMessage message) {
        message.setTimestamp(Instant.now());
        log.info("Received message: {}", message);
        return message;
    }

    @MessageMapping("/send/private")
    public void sendPrivateMessage(PrivateChatMessage message, Principal principal) {
        log.info("Principal name: {}", principal.getName());
        log.info("SenderId: {}", message.getSenderId());
        if (!principal.getName().equals(message.getSenderId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        message.setTimestamp(Instant.now());

        messagingTemplate.convertAndSend(
                "/queue/private-messages-user" + message.getReceiverId(),
                message
        );
    }
}
