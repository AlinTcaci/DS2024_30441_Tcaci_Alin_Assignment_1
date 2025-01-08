package ds2024.chat.controller;


import ds2024.chat.dtos.ChatMessageDTO;
import ds2024.chat.dtos.builder.ChatMessageBuilder;
import ds2024.chat.entity.ChatMessage;
import ds2024.chat.repositories.ChatMessageRepository;
import ds2024.chat.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = ChatMessageBuilder.toEntity(chatMessageDTO);
        chatMessage.setReadStatus(false);
        chatMessageRepository.save(chatMessage);
        return ChatMessageBuilder.toChatMessageDTO(chatMessage);
    }


    @GetMapping("/api/messages")
    public List<ChatMessageDTO> getMessages(@RequestParam String sender, @RequestParam String receiver) {
        return chatMessageService.findChatsBySenderAndReceiver(sender, receiver);
    }

    @PutMapping("/api/messages/read/{id}")
    public void markMessageAsRead(@PathVariable UUID id) {
        chatMessageService.markAsRead(id);
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public String notifyTyping(String typingNotification) {
        return typingNotification;
    }


}
