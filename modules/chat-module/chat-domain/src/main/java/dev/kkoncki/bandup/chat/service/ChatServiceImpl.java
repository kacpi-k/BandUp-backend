package dev.kkoncki.bandup.chat.service;

import dev.kkoncki.bandup.chat.GroupChatMessage;
import dev.kkoncki.bandup.chat.PrivateChatMessage;
import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import dev.kkoncki.bandup.chat.repository.ChatRepository;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;

    public ChatServiceImpl(ChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public void savePrivateMessage(SendPrivateChatMessageForm form) {
        PrivateChatMessage message = PrivateChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .senderId(form.getSenderId())
                .receiverId(form.getReceiverId())
                .content(form.getContent())
                .timestamp(Instant.now())
                .isRead(false)
                .build();

        repository.savePrivateMessage(message);
    }

    @Override
    public void saveGroupMessage(SendGroupChatMessageForm form) {
        GroupChatMessage message = GroupChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .senderId(form.getSenderId())
                .bandId(form.getBandId())
                .content(form.getContent())
                .timestamp(Instant.now())
                .build();

        repository.saveGroupMessage(message);
    }

    @Override
    public Page<PrivateChatMessage> getPrivateMessages(String senderId, String receiverId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repository.findPrivateMessages(senderId, receiverId, pageable);
    }

    @Override
    public void markMessageAsRead(String messageId) {
        PrivateChatMessage message = repository.findById(messageId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.MESSAGE_NOT_FOUND));
        message.setRead(true);
        repository.savePrivateMessage(message);
    }

    @Override
    public Page<GroupChatMessage> getGroupMessages(String bandId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repository.findGroupMessages(bandId, pageable);

    }
}
