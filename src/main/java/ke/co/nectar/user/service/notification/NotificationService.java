package ke.co.nectar.user.service.notification;

import ke.co.nectar.user.controllers.NotificationsController;
import ke.co.nectar.user.entity.Notification;
import ke.co.nectar.user.entity.notifications.NotificationUser;

import java.time.Instant;
import java.util.List;

public interface NotificationService {

    List<NotificationsController.MessageNotification>
        getNotifications(String userRef) throws Exception;

    String add(Notification notification) throws Exception;

    Notification findByRef(String notificationRef) throws Exception;

    void updateNotificationUser(NotificationUser notificationUser) throws Exception;

    void setNotificationStatus(String userRef,
                               String notificationRef,
                               boolean status,
                               Instant readTimestamp) throws Exception;
}
