package ds2024.chat.repositories;

import ds2024.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    @Query("SELECT cm FROM ChatMessage cm WHERE (cm.sender = :sender AND cm.receiver = :receiver) OR (cm.sender = :receiver AND cm.receiver = :sender)")
    List<ChatMessage> findMessagesBySenderAndReceiver(@Param("sender") String sender, @Param("receiver") String receiver);

}
