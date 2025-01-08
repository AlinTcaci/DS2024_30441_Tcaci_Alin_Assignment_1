package ds2024.chat.dtos.builder;


import ds2024.chat.dtos.ChatMessageDTO;
import ds2024.chat.entity.ChatMessage;

public class ChatMessageBuilder {

    public static ChatMessage toEntity(ChatMessageDTO chatMessageDTO){
        return new ChatMessage(chatMessageDTO.getId(),chatMessageDTO.getReceiver(),chatMessageDTO.getSender(),chatMessageDTO.getContent(), chatMessageDTO.getTimestamp(), chatMessageDTO.isReadStatus(), chatMessageDTO.getReadTimestamp());
    }

    public static ChatMessageDTO toChatMessageDTO(ChatMessage chatMessage){
        return new ChatMessageDTO(chatMessage.getId(),chatMessage.getReceiver(),chatMessage.getSender(),chatMessage.getContent(), chatMessage.getTimestamp(), chatMessage.isReadStatus(), chatMessage.getReadTimestamp());
    }

}
