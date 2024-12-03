package ds2024.monitoring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationHandler extends TextWebSocketHandler {
    public void sendNotification(String message) {
        for (WebSocketSession session : WebSocketSessionManager.getSessions()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("Could not send the message to frontend");
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketSessionManager.getSessions().add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSessionManager.getSessions().remove(session);
    }
}