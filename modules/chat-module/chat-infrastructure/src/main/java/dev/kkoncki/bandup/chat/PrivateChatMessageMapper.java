package dev.kkoncki.bandup.chat;

import java.util.List;

public class PrivateChatMessageMapper {

    public static PrivateChatMessage toDomain(PrivateChatMessageEntity entity) {
        return PrivateChatMessage.builder()
                .id(entity.getId())
                .senderId(entity.getSenderId())
                .receiverId(entity.getReceiverId())
                .content(entity.getContent())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public static PrivateChatMessageEntity toEntity(PrivateChatMessage message) {
        return PrivateChatMessageEntity.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }

    public static List<PrivateChatMessage> toDomainList(List<PrivateChatMessageEntity> entities) {
        return entities.stream()
                .map(PrivateChatMessageMapper::toDomain)
                .toList();
    }
}
