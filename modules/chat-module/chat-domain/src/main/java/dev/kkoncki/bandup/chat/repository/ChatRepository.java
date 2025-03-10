package dev.kkoncki.bandup.chat.repository;

import dev.kkoncki.bandup.chat.GroupChatMessage;
import dev.kkoncki.bandup.chat.PrivateChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChatRepository {
    void savePrivateMessage(PrivateChatMessage message);
    void saveGroupMessage(GroupChatMessage message);
    Page<PrivateChatMessage> findPrivateMessages(String senderId, String receiverId, Pageable pageable);
    Optional<PrivateChatMessage> findById(String messageId);
    Page<GroupChatMessage> findGroupMessages(String bandId, Pageable pageable);
}
