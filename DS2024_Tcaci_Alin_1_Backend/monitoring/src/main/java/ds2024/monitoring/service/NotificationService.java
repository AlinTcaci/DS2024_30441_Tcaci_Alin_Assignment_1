package ds2024.monitoring.service;

import ds2024.monitoring.config.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationHandler notificationHandler;

    @Autowired
    public NotificationService(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    public void sendExceedNotification(String message) {
        notificationHandler.sendNotification(message);
    }
}