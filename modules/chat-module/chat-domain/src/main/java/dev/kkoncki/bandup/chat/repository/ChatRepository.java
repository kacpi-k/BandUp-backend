package dev.kkoncki.bandup.chat.repository;

import dev.kkoncki.bandup.chat.GroupChatMessage;
import dev.kkoncki.bandup.chat.PrivateChatMessage;

import java.util.List;

public interface ChatRepository {
    void savePrivateMessage(PrivateChatMessage message);
    void saveGroupMessage(GroupChatMessage message);
    List<PrivateChatMessage> findPrivateMessages(String senderId, String receiverId);
    List<GroupChatMessage> findGroupMessages(String bandId);
}
