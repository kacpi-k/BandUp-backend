package dev.kkoncki.bandup.chat;

import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import dev.kkoncki.bandup.chat.repository.ChatRepository;
import dev.kkoncki.bandup.chat.service.ChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    private SendPrivateChatMessageForm privateMessageForm;
    private SendGroupChatMessageForm groupMessageForm;

    @BeforeEach
    void setUp() {
        privateMessageForm = new SendPrivateChatMessageForm(
                "sender-id",
                "receiver-id",
                "Hello, private chat!"
        );

        groupMessageForm = new SendGroupChatMessageForm(
                "sender-id",
                "band-id",
                "Hello, group chat!"
        );
    }

    @Test
    void shouldSavePrivateMessage() {
        chatService.savePrivateMessage(privateMessageForm);

        verify(chatRepository, times(1)).savePrivateMessage(any(PrivateChatMessage.class));
    }

    @Test
    void shouldSaveGroupMessage() {
        chatService.saveGroupMessage(groupMessageForm);

        verify(chatRepository, times(1)).saveGroupMessage(any(GroupChatMessage.class));
    }

    @Test
    void shouldRetrievePrivateMessages() {
        String senderId = "sender-id";
        String receiverId = "receiver-id";

        List<PrivateChatMessage> messages = List.of(
                PrivateChatMessage.builder().id("1").senderId(senderId).receiverId(receiverId).content("Hi!").timestamp(Instant.now()).build(),
                PrivateChatMessage.builder().id("2").senderId(receiverId).receiverId(senderId).content("Hello!").timestamp(Instant.now()).build()
        );

        when(chatRepository.findPrivateMessages(senderId, receiverId)).thenReturn(messages);

        List<PrivateChatMessage> result = chatService.getPrivateMessages(senderId, receiverId);

        assertEquals(2, result.size());
        verify(chatRepository, times(1)).findPrivateMessages(senderId, receiverId);
    }

    @Test
    void shouldRetrieveGroupMessages() {
        String bandId = "band-id";

        List<GroupChatMessage> messages = List.of(
                GroupChatMessage.builder().id("1").bandId(bandId).senderId("user-1").content("Hi Band!").timestamp(Instant.now()).build(),
                GroupChatMessage.builder().id("2").bandId(bandId).senderId("user-2").content("Hello Everyone!").timestamp(Instant.now()).build()
        );

        when(chatRepository.findGroupMessages(bandId)).thenReturn(messages);

        List<GroupChatMessage> result = chatService.getGroupMessages(bandId);

        assertEquals(2, result.size());
        verify(chatRepository, times(1)).findGroupMessages(bandId);
    }
}
