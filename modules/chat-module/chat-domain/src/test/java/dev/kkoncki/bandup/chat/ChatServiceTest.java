package dev.kkoncki.bandup.chat;

import dev.kkoncki.bandup.chat.forms.SendGroupChatMessageForm;
import dev.kkoncki.bandup.chat.forms.SendPrivateChatMessageForm;
import dev.kkoncki.bandup.chat.repository.ChatRepository;
import dev.kkoncki.bandup.chat.service.ChatServiceImpl;
import dev.kkoncki.bandup.commons.LoggedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private LoggedUser loggedUser;

    @InjectMocks
    private ChatServiceImpl chatService;

    private SendPrivateChatMessageForm privateMessageForm;
    private SendGroupChatMessageForm groupMessageForm;

    @BeforeEach
    void setUp() {
        lenient().when(loggedUser.getUserId()).thenReturn("sender-id");

        privateMessageForm = new SendPrivateChatMessageForm(
                "receiver-id",
                "Hello, private chat!"
        );

        groupMessageForm = new SendGroupChatMessageForm(
                "band-id",
                "Hello, group chat!"
        );
    }

    @Test
    void shouldSavePrivateMessage() {
        chatService.savePrivateMessage(privateMessageForm, loggedUser.getUserId());

        verify(chatRepository, times(1)).savePrivateMessage(any(PrivateChatMessage.class));
    }

    @Test
    void shouldMarkMessageAsRead() {
        String messageId = "message-id";
        PrivateChatMessage message = PrivateChatMessage.builder()
                .id(messageId)
                .senderId("sender-id")
                .receiverId("receiver-id")
                .content("Test message")
                .timestamp(Instant.now())
                .isRead(false)
                .build();

        when(chatRepository.findById(messageId)).thenReturn(Optional.of(message));

        chatService.markMessageAsRead(messageId);

        assertTrue(message.isRead());
        verify(chatRepository, times(1)).savePrivateMessage(message);
    }


    @Test
    void shouldSaveGroupMessage() {
        chatService.saveGroupMessage(groupMessageForm, loggedUser.getUserId());

        verify(chatRepository, times(1)).saveGroupMessage(any(GroupChatMessage.class));
    }

    @Test
    void shouldRetrievePrivateMessages() {
        String senderId = "sender-id";
        String receiverId = "receiver-id";
        Pageable pageable = PageRequest.of(0, 20, Sort.by("timestamp").descending());

        List<PrivateChatMessage> messages = List.of(
                PrivateChatMessage.builder().id("1").senderId(senderId).receiverId(receiverId).content("Hi!").timestamp(Instant.now()).isRead(false).build(),
                PrivateChatMessage.builder().id("2").senderId(receiverId).receiverId(senderId).content("Hello!").timestamp(Instant.now()).isRead(false).build()
        );

        Page<PrivateChatMessage> messagePage = new PageImpl<>(messages, pageable, messages.size());

        when(chatRepository.findPrivateMessages(senderId, receiverId, pageable)).thenReturn(messagePage);

        Page<PrivateChatMessage> result = chatService.getPrivateMessages(senderId, receiverId, 0, 20);

        assertEquals(2, result.getContent().size());
        verify(chatRepository, times(1)).findPrivateMessages(senderId, receiverId, pageable);
    }


    @Test
    void shouldRetrieveGroupMessages() {
        String bandId = "band-id";
        Pageable pageable = PageRequest.of(0, 20, Sort.by("timestamp").descending());

        List<GroupChatMessage> messages = List.of(
                GroupChatMessage.builder().id("1").bandId(bandId).senderId("user-1").content("Hi Band!").timestamp(Instant.now()).build(),
                GroupChatMessage.builder().id("2").bandId(bandId).senderId("user-2").content("Hello Everyone!").timestamp(Instant.now()).build()
        );

        Page<GroupChatMessage> messagePage = new PageImpl<>(messages, pageable, messages.size());

        when(chatRepository.findGroupMessages(bandId, pageable)).thenReturn(messagePage);

        Page<GroupChatMessage> result = chatService.getGroupMessages(bandId, 0, 20);

        assertEquals(2, result.getContent().size());
        verify(chatRepository, times(1)).findGroupMessages(bandId, pageable);
    }
}
