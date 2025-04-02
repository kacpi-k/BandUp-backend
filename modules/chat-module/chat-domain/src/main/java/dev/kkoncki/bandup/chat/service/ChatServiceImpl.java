package dev.kkoncki.bandup.chat.service;

import dev.kkoncki.bandup.band.BandMember;
import dev.kkoncki.bandup.band.repository.BandMemberRepository;
import dev.kkoncki.bandup.chat.GroupChatMessage;
import dev.kkoncki.bandup.chat.PrivateChatMessage;
import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import dev.kkoncki.bandup.chat.repository.ChatRepository;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;
    private final BandMemberRepository bandMemberRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    public ChatServiceImpl(ChatRepository repository, BandMemberRepository bandMemberRepository) {
        this.repository = repository;
        this.bandMemberRepository = bandMemberRepository;
    }

    @Override
    public void savePrivateMessage(SendPrivateChatMessageForm form, String senderId) {
        PrivateChatMessage message = PrivateChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .senderId(senderId)
                .receiverId(form.getReceiverId())
                .content(form.getContent())
                .timestamp(Instant.now())
                .isRead(false)
                .build();

        repository.savePrivateMessage(message);
    }

    @Override
    public void saveGroupMessage(SendGroupChatMessageForm form, String senderId) {
        GroupChatMessage message = GroupChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .senderId(senderId)
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

    @Transactional
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

    @Transactional
    @Override
    public List<PrivateChatMessage> getAllUserPrivateMessages(String userId) {
        return repository.findAllBySenderIdOrReceiverId(userId, userId);
    }

    @Transactional
    @Override
    public List<GroupChatMessage> getAllUserGroupMessages(String userId) {
        List<String> userBandIds = bandMemberRepository.findAllByUserId(userId)
                .stream()
                .map(BandMember::getBandId)
                .collect(Collectors.toList());

        logger.debug("User band IDs: {}", userBandIds);

        List<GroupChatMessage> messages = repository.findAllByBandIdIn(userBandIds);

        logger.debug("Group messages: {}", messages);

        return repository.findAllByBandIdIn(userBandIds);
    }
}
