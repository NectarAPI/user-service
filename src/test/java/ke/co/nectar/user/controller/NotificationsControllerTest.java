package ke.co.nectar.user.controller;

import ke.co.nectar.user.NectarUserServiceApplication;
import ke.co.nectar.user.controllers.NotificationsController;
import ke.co.nectar.user.entity.Notification;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.notifications.NotificationUser;
import ke.co.nectar.user.service.notification.NotificationService;
import ke.co.nectar.user.service.user.UserService;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarUserServiceApplication.class)
@FixMethodOrder(MethodSorters.JVM)
@Transactional
public class NotificationsControllerTest {

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {}

    @Test
    public void saveNotificationWithUser() throws Exception {
        final long EPOCH_TIME = 1606754076302L;
        final String CREATE_USER_REF = "f249dfea-f1b2-4145-b848-6d2004f112d6";

        User user = new User("anotherFirstName", "anotherLastName", "anotherUsername",
                "0323232", "https://anotherimage.com",
                "630a5940-5d2f-48c0-bafa-27d276107091", "remember_token",
                "another-email@aa.com", "2121212", true);
        String userRef = userService.add(user, CREATE_USER_REF);

        Notification notification = new Notification();
        notification.setSubject("subject");
        notification.setText("text");
        notification.setType(Notification.NotificationType.INFO);
        notification.setAffected("tokens-service, notifications-service");
        notification.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        String notificationRef = notificationService.add(notification);

        NotificationUser notificationUser = new NotificationUser();
        notificationUser.setUserRef(userRef);
        notificationUser.setNotificationRef(notificationRef);
        notificationUser.setRead(true);
        notificationUser.setReadDate(Instant.ofEpochMilli(EPOCH_TIME));

        notificationService.updateNotificationUser(notificationUser);

        List<NotificationsController.MessageNotification>
                messageNotifications = notificationService.getNotifications(userRef);

        Assert.assertEquals(1, messageNotifications.size());
        Assert.assertEquals(userRef, messageNotifications.get(0).getUserRef());
        Assert.assertEquals(Instant.ofEpochMilli(EPOCH_TIME), messageNotifications.get(0).getReadDate());
        Assert.assertEquals("subject", messageNotifications.get(0).getSubject());
        Assert.assertEquals("text", messageNotifications.get(0).getText());
        Assert.assertEquals("INFO", messageNotifications.get(0).getType());
        Assert.assertEquals("tokens-service, notifications-service", messageNotifications.get(0).getAffected());
        Assert.assertEquals(Instant.ofEpochMilli(EPOCH_TIME), messageNotifications.get(0).getCreatedDate());
        Assert.assertTrue(messageNotifications.get(0).isRead());

    }

    @Test
    public void getNotificationByRef() throws Exception {

        Notification notification = new Notification();
        notification.setSubject("subject");
        notification.setText("text");
        notification.setType(Notification.NotificationType.INFO);
        notification.setAffected("tokens-service, notifications-service");
        String notificationRef = notificationService.add(notification);

        Notification obtainedNotification = notificationService.findByRef(notificationRef);

        Assert.assertNotNull(obtainedNotification);
        Assert.assertEquals("subject", obtainedNotification.getSubject());
        Assert.assertEquals("text", obtainedNotification.getText());
        Assert.assertEquals("tokens-service, notifications-service", obtainedNotification.getAffected());
        Assert.assertEquals(Notification.NotificationType.INFO, obtainedNotification.getType());

    }

    @Test
    public void updateNotificationUser() throws Exception {
        final long EPOCH_TIME = 1606754076302l;
        final String USER_REF = "630a5940-5d2f-48c0-bafa-27d276107091";
        final String CREATE_USER_REF = "f249dfea-f1b2-4145-b848-6d2004f112d6";

        User user = new User("anotherFirstName", "anotherLastName", "anotherUsername",
                "0323232", "https://anotherimage.com",
                USER_REF, "remember_token",
                "another-email@aa.com", "2121212", true);
        String userRef = userService.add(user, CREATE_USER_REF);

        Notification notification = new Notification();
        notification.setSubject("subject");
        notification.setText("text");
        notification.setType(Notification.NotificationType.INFO);
        notification.setAffected("tokens-service, notifications-service");
        String notificationRef = notificationService.add(notification);

        NotificationUser notificationUser = new NotificationUser();
        notificationUser.setUserRef(userRef);
        notificationUser.setNotificationRef(notificationRef);
        notificationUser.setRead(false);
        notificationUser.setReadDate(Instant.ofEpochMilli(EPOCH_TIME));
        notificationService.updateNotificationUser(notificationUser);

        notificationService.setNotificationStatus(userRef, notificationRef, true, Instant.ofEpochMilli(EPOCH_TIME));

        List<NotificationsController.MessageNotification>
            notifications = notificationService.getNotifications(userRef);

        Assert.assertEquals(1, notifications.size());
        Assert.assertTrue(notifications.get(0).isRead());
    }
}
