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
@Table(name = "group_chat_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupChatMessageEntity {

    @Id
    private String id;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "band_id")
    private String bandId;

    private String content;

    private Instant timestamp;
}
