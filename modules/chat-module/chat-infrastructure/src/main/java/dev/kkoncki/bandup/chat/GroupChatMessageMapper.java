package dev.kkoncki.bandup.chat;

import java.util.List;

public class GroupChatMessageMapper {

    public static GroupChatMessage toDomain(GroupChatMessageEntity entity) {
        return GroupChatMessage.builder()
                .id(entity.getId())
                .senderId(entity.getSenderId())
                .bandId(entity.getBandId())
                .content(entity.getContent())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public static GroupChatMessageEntity toEntity(GroupChatMessage message) {
        return GroupChatMessageEntity.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .bandId(message.getBandId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }

    public static List<GroupChatMessage> toDomainList(List<GroupChatMessageEntity> entities) {
        return entities.stream()
                .map(GroupChatMessageMapper::toDomain)
                .toList();
    }
}
