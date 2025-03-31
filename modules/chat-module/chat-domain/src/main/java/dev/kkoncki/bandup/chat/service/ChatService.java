package dev.kkoncki.bandup.chat.service;

import dev.kkoncki.bandup.chat.GroupChatMessage;
import dev.kkoncki.bandup.chat.PrivateChatMessage;
import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ChatService {
    void savePrivateMessage(@Valid SendPrivateChatMessageForm form, String senderId);
    void saveGroupMessage(@Valid SendGroupChatMessageForm form, String senderId);
    Page<PrivateChatMessage> getPrivateMessages(String senderId, String receiverId, int page, int size);
    void markMessageAsRead(String messageId);
    Page<GroupChatMessage> getGroupMessages(String bandId, int page, int size);
    List<PrivateChatMessage> getAllUserPrivateMessages(String userId);
    List<GroupChatMessage> getAllUserGroupMessages(String userId);
}
