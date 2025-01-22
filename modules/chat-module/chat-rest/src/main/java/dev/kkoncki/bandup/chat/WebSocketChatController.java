package dev.kkoncki.bandup.chat;

import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import dev.kkoncki.bandup.chat.service.ChatService;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;

@Slf4j
@Controller
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public WebSocketChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public GroupChatMessage sendMessage(SendGroupChatMessageForm form, Principal principal) {
        if (!principal.getName().equals(form.getSenderId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        chatService.saveGroupMessage(form);

        GroupChatMessage message = GroupChatMessage.builder()
                .senderId(form.getSenderId())
                .bandId(form.getBandId())
                .content(form.getContent())
                .timestamp(Instant.now())
                .build();

        log.info("Received group message: {}", message);
        return message;
    }

    @MessageMapping("/send/private")
    public void sendPrivateMessage(SendPrivateChatMessageForm form, Principal principal) {
        if (!principal.getName().equals(form.getSenderId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        chatService.savePrivateMessage(form);

        PrivateChatMessage message = PrivateChatMessage.builder()
                .senderId(form.getSenderId())
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
