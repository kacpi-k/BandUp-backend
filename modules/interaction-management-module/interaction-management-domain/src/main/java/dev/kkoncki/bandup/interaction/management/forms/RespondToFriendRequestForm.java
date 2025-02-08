package dev.kkoncki.bandup.interaction.management.forms;

import dev.kkoncki.bandup.interaction.management.FriendshipStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespondToFriendRequestForm {
    @NotBlank
    @Size(min = 36, max = 36)
    private String friendshipId;

    @NotNull
    private FriendshipStatus status;
}
