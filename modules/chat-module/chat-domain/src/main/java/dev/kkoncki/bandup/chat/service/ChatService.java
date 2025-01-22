package dev.kkoncki.bandup.chat.service;

import dev.kkoncki.bandup.chat.GroupChatMessage;
import dev.kkoncki.bandup.chat.PrivateChatMessage;
import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ChatService {
    void savePrivateMessage(@Valid SendPrivateChatMessageForm form);
    void saveGroupMessage(@Valid SendGroupChatMessageForm form);
    List<PrivateChatMessage> getPrivateMessages(String senderId, String receiverId);
    List<GroupChatMessage> getGroupMessages(String bandId);
}
