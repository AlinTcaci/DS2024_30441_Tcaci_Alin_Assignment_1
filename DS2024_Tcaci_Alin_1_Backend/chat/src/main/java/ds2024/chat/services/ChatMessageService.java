package ds2024.chat.services;


import ds2024.chat.dtos.ChatMessageDTO;
import ds2024.chat.dtos.builder.ChatMessageBuilder;
import ds2024.chat.entity.ChatMessage;
import ds2024.chat.repositories.ChatMessageRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatMessageService.class);

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Transactional
    public List<ChatMessageDTO> findChatsBySenderAndReceiver(String sender, String receiver) {
        // Fetching messages based on sender and receiver
        List<ChatMessage> chatMessageListAux = chatMessageRepository.findAll();
        List<ChatMessage> chatMessageList = new ArrayList<>();

        // Filter messages where sender and receiver match
        for (ChatMessage chatMessage : chatMessageListAux) {
            if ((chatMessage.getSender().equals(sender) && chatMessage.getReceiver().equals(receiver)) ||
                    (chatMessage.getSender().equals(receiver) && chatMessage.getReceiver().equals(sender))) {
                chatMessageList.add(chatMessage);
            }
        }

        // Sort messages by timestamp
        chatMessageList.sort(Comparator.comparing(ChatMessage::getTimestamp));

        // Convert entities to DTOs
        return chatMessageList.stream()
                .map(ChatMessageBuilder::toChatMessageDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public UUID insert(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = ChatMessageBuilder.toEntity(chatMessageDTO);
        chatMessage.setReadStatus(false);
        chatMessage = chatMessageRepository.save(chatMessage);
        LOGGER.debug("ChatMessage with id {} was inserted in db", chatMessage.getId());
        return chatMessage.getId();
    }

    @Transactional
    public void markAsRead(UUID id) {
        ChatMessage message = chatMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setReadStatus(true);
        message.setReadTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        chatMessageRepository.save(message);

        // Notify clients via WebSocket
        simpMessagingTemplate.convertAndSend("/topic/readStatus", message.getId());
    }
}
