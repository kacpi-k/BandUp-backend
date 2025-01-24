package dev.kkoncki.bandup.post.management.forms;

import dev.kkoncki.bandup.post.management.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostForm {

    @NotBlank(message = "User ID must not be blank")
    @Size(min = 36, max = 36, message = "User ID must have 36 characters.")
    private String userId;

    @NotBlank(message = "Content must not be blank")
    @Size(max = 5000, message = "Content must have at most 5000 characters.")
    private String content;

    @NotBlank(message = "Media URL must not be blank")
    private String mediaUrl;

}
