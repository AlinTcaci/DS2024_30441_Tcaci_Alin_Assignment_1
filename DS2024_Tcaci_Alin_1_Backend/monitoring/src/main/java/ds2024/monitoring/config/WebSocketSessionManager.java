package ds2024.monitoring.config;


import org.springframework.web.socket.WebSocketSession;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebSocketSessionManager {

    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public static Set<WebSocketSession> getSessions() {
        return sessions;
    }
}