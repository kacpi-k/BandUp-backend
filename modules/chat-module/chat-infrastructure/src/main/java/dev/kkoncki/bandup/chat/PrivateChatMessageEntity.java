package dev.kkoncki.bandup.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "private_chat_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivateChatMessageEntity {

    @Id
    private String id;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "receiver_id")
    private String receiverId;

    private String content;

    private Instant timestamp;
}
