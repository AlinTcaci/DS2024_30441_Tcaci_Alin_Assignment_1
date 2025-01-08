package ds2024.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    @Column(name="id", columnDefinition = "BINARY(16)")
    private UUID id;
    @Column
    private String receiver;
    @Column
    private String sender;
    @Column
    private String content;
    @Column
    private String timestamp;
    @Column
    private boolean readStatus;
    @Column
    private String readTimestamp;

    public ChatMessage(UUID id, String receiver, String sender, String content, String timestamp, boolean readStatus, String readTimestamp) {
        this.id = id;
        this.receiver = receiver;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.readStatus = readStatus;
        this.readTimestamp = readTimestamp;
    }

    public ChatMessage() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getReadTimestamp() {
        return readTimestamp;
    }

    public void setReadTimestamp(String readTimestamp) {
        this.readTimestamp = readTimestamp;
    }
}
