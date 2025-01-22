package dev.kkoncki.bandup.chat.service;

import dev.kkoncki.bandup.chat.GroupChatMessage;
import dev.kkoncki.bandup.chat.PrivateChatMessage;
import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import dev.kkoncki.bandup.chat.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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
    public List<PrivateChatMessage> getPrivateMessages(String senderId, String receiverId) {
        return repository.findPrivateMessages(senderId, receiverId);
    }

    @Override
    public List<GroupChatMessage> getGroupMessages(String bandId) {
        return repository.findGroupMessages(bandId);
    }
}
