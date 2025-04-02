package dev.kkoncki.bandup.chat.repository;

import dev.kkoncki.bandup.chat.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ChatRepositoryAdapter implements ChatRepository {

    private final JpaPrivateChatRepository privateRepo;
    private final JpaGroupChatRepository groupRepo;
    private final JpaPrivateChatRepository jpaPrivateChatRepository;

    public ChatRepositoryAdapter(JpaPrivateChatRepository privateRepo, JpaGroupChatRepository groupRepo, JpaPrivateChatRepository jpaPrivateChatRepository) {
        this.privateRepo = privateRepo;
        this.groupRepo = groupRepo;
        this.jpaPrivateChatRepository = jpaPrivateChatRepository;
    }


    @Override
    public void savePrivateMessage(PrivateChatMessage message) {
        PrivateChatMessageEntity entity = PrivateChatMessageMapper.toEntity(message);
        privateRepo.save(entity);
    }

    @Override
    public void saveGroupMessage(GroupChatMessage message) {
        GroupChatMessageEntity entity = GroupChatMessageMapper.toEntity(message);
        groupRepo.save(entity);
    }

    @Override
    public Page<PrivateChatMessage> findPrivateMessages(String senderId, String receiverId, Pageable pageable) {
        Page<PrivateChatMessageEntity> entityPage = privateRepo.findPrivateMessages(senderId, receiverId, pageable);

        List<PrivateChatMessage> domainMessages = entityPage.getContent()
                .stream()
                .map(PrivateChatMessageMapper::toDomain)
                .toList();

        return new PageImpl<>(domainMessages, pageable, entityPage.getTotalElements());
    }

    @Override
    public Optional<PrivateChatMessage> findById(String messageId) {
        return jpaPrivateChatRepository.findById(messageId)
                .map(PrivateChatMessageMapper::toDomain);
    }

    @Override
    public Page<GroupChatMessage> findGroupMessages(String bandId, Pageable pageable) {
        Page<GroupChatMessageEntity> entityPage = groupRepo.findGroupMessages(bandId, pageable);

        List<GroupChatMessage> domainMessages = entityPage.getContent()
                .stream()
                .map(GroupChatMessageMapper::toDomain)
                .toList();

        return new PageImpl<>(domainMessages, pageable, entityPage.getTotalElements());
    }

    @Override
    public List<PrivateChatMessage> findAllBySenderIdOrReceiverId(String userId, String userId1) {
        List<PrivateChatMessageEntity> entities = privateRepo.findAllBySenderIdOrReceiverId(userId, userId1);

        return entities.stream()
                .map(PrivateChatMessageMapper::toDomain)
                .toList();
    }

    @Override
    public List<GroupChatMessage> findAllByBandIdIn(List<String> userBandIds) {
        List<GroupChatMessageEntity> entities = groupRepo.findAllByBandIdIn(userBandIds);

        return GroupChatMessageMapper.toDomainList(entities);
    }
}
