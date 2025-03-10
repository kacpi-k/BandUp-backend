package dev.kkoncki.bandup.chat.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendPrivateChatMessageForm {

    @NotBlank(message = "Receiver ID must not be blank")
    @Size(min = 36, max = 36, message = "Receiver ID must have 36 characters.")
    private String receiverId;

    @NotBlank(message = "Content must not be blank")
    private String content;
}
