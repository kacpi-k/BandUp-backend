package dev.kkoncki.bandup.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupChatMessage {
    private String id;
    private String senderId;
    private String bandId;
    private String content;
    private Instant timestamp;
}
