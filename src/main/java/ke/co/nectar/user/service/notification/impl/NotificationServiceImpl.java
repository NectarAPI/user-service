package ke.co.nectar.user.service.notification.impl;

import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.controllers.NotificationsController;
import ke.co.nectar.user.entity.Notification;
import ke.co.nectar.user.entity.notifications.NotificationUser;
import ke.co.nectar.user.repository.NotificationRepository;
import ke.co.nectar.user.repository.NotificationUserRepository;
import ke.co.nectar.user.service.notification.NotificationService;
import ke.co.nectar.user.service.notification.impl.exceptions.InvalidNotificationException;
import ke.co.nectar.user.service.notification.impl.exceptions.InvalidNotificationRefException;
import ke.co.nectar.user.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @Override
    public String add(Notification notification) throws Exception {
        if (notification != null) {
            if (notification.getRef() == null) {
                String ref = AppUtils.generateRef();
                notification.setRef(ref);
            }
            notificationRepository.save(notification);
            return notification.getRef();
        }
        throw new InvalidNotificationException(StringConstants.INVALID_NOTIFICATION);
    }

    @Override
    public List<NotificationsController.MessageNotification>
            getNotifications(String userRef) throws Exception {
        List<NotificationUser> userNotifications
                = notificationUserRepository.findByUserRef(userRef);
        List<NotificationsController.MessageNotification> notifications = new ArrayList<>();

        for (NotificationUser notificationUser : userNotifications) {
            String notificationRef = notificationUser.getNotificationRef();
            Notification notification = notificationRepository.findByRef(notificationRef);

            if (notification != null) {
                NotificationsController.MessageNotification obtainedNotification = new NotificationsController.MessageNotification();
                obtainedNotification.setRef(notification.getRef());
                obtainedNotification.setSubject(notification.getSubject());
                obtainedNotification.setText(notification.getText());
                obtainedNotification.setType(notification.getType().toString());
                obtainedNotification.setAffected(notification.getAffected());
                obtainedNotification.setRead(notificationUser.isRead());
                obtainedNotification.setReadDate(notificationUser.getReadDate());
                obtainedNotification.setUserRef(userRef);
                obtainedNotification.setCreatedDate(notification.getCreatedAt());

                notifications.add(obtainedNotification);

            } else {
                throw new InvalidNotificationRefException(
                        String.format(StringConstants.INVALID_NOTIFICATION_REF, notificationRef));
            }
        }
        return notifications;
    }

    @Override
    public Notification findByRef(String notificationRef) throws Exception {
        if (notificationRef != null) {
            return notificationRepository.findByRef(notificationRef);
        }
        throw new InvalidNotificationRefException(
                StringConstants.INVALID_NOTIFICATION_REF);
    }

    @Override
    public void updateNotificationUser(NotificationUser notificationUser) throws Exception {
        notificationUserRepository.save(notificationUser);
    }

    @Override
    public void setNotificationStatus(String userRef,
                                      String notificationRef,
                                      boolean status,
                                      Instant readTimestamp) throws Exception {
        notificationUserRepository.setNotificationStatus(userRef, notificationRef, status, readTimestamp);
    }


}
