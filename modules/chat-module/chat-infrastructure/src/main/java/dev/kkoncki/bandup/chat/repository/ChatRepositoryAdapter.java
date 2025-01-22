package dev.kkoncki.bandup.chat.repository;

import dev.kkoncki.bandup.chat.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatRepositoryAdapter implements ChatRepository {

    private final JpaPrivateChatRepository privateRepo;
    private final JpaGroupChatRepository groupRepo;

    public ChatRepositoryAdapter(JpaPrivateChatRepository privateRepo, JpaGroupChatRepository groupRepo) {
        this.privateRepo = privateRepo;
        this.groupRepo = groupRepo;
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
    public List<PrivateChatMessage> findPrivateMessages(String senderId, String receiverId) {
        List<PrivateChatMessageEntity> entities = privateRepo.findPrivateMessages(senderId, receiverId);

        return PrivateChatMessageMapper.toDomainList(entities);
    }

    @Override
    public List<GroupChatMessage> findGroupMessages(String bandId) {
        List<GroupChatMessageEntity> entities = groupRepo.findGroupMessages(bandId);

        return GroupChatMessageMapper.toDomainList(entities);
    }
}
