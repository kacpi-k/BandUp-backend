package dev.kkoncki.bandup.chat.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendGroupChatMessageForm {

    @NotBlank(message = "Sender ID must not be blank")
    @Size(min = 36, max = 36, message = "Sender ID must have 36 characters.")
    private String senderId;

    @NotBlank(message = "Band ID must not be blank")
    @Size(min = 36, max = 36, message = "Band ID must have 36 characters.")
    private String bandId;

    @NotBlank(message = "Content must not be blank")
    private String content;
}
