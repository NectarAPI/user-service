package ke.co.nectar.user.service;

import ke.co.nectar.user.NectarUserServiceApplication;
import ke.co.nectar.user.controllers.NotificationsController;
import ke.co.nectar.user.entity.Notification;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.notifications.NotificationUser;
import ke.co.nectar.user.repository.NotificationRepository;
import ke.co.nectar.user.repository.NotificationUserRepository;
import ke.co.nectar.user.repository.UserRepository;
import ke.co.nectar.user.service.notification.NotificationService;
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
public class NotificationsServiceTest {

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationUserRepository notificationUserRepository;

    @Autowired
    UserRepository userRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String USER_REF = "630a5940-5d2f-48c0-bafa-27d276107091";
    private final String NOTIFICATION_REF = "ea91088c-8cc3-436c-8429-70831daed600";
    private final long EPOCH_TIME = 1606754076302l;

    @Before
    public void setup() {
        User user = new User("anotherFirstName", "anotherLastName", "anotherUsername",
                "0323232", "https://anotherimage.com",
                USER_REF, "remember_token",
                "another-email@aa.com", "2121212", true);
        userRepository.save(user);

        Notification notification = new Notification();
        notification.setSubject("subject");
        notification.setText("text");
        notification.setType(Notification.NotificationType.INFO);
        notification.setAffected("tokens-service, notifications-service");
        notification.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        notification.setRef(NOTIFICATION_REF);
        notificationRepository.save(notification);

        NotificationUser notificationUser = new NotificationUser();
        notificationUser.setUserRef(USER_REF);
        notificationUser.setNotificationRef(NOTIFICATION_REF);
        notificationUser.setRead(false);

        notificationUserRepository.save(notificationUser);
    }

    @Test
    public void testThatNotificationsAreReceived() throws Exception {
        List<NotificationsController.MessageNotification> notifications
            = notificationService.getNotifications(USER_REF);
        Assert.assertEquals(1, notifications.size());
        Assert.assertEquals(USER_REF, notifications.get(0).getUserRef());
        Assert.assertEquals("text", notifications.get(0).getText());
        Assert.assertEquals("subject", notifications.get(0).getSubject());
        Assert.assertEquals("INFO", notifications.get(0).getType());
        Assert.assertNotNull(notifications.get(0).getRef());
        Assert.assertFalse(notifications.get(0).isRead());
        Assert.assertEquals(null, notifications.get(0).getReadDate());
    }

    @Test
    public void testThatNotificationIsFound() throws Exception {
        final String REF = "62721bd2-b29e-4776-af3f-ad6e95fdfe8a";

        Notification notification = new Notification();
        notification.setSubject("subject1");
        notification.setText("text1");
        notification.setType(Notification.NotificationType.INFO);
        notification.setAffected("tokens-service");
        notification.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        notification.setRef(REF);

        notificationService.add(notification);

        Notification obtainedNotification = notificationService.findByRef(REF);

        Assert.assertEquals("subject1", obtainedNotification.getSubject());
        Assert.assertEquals("text1", obtainedNotification.getText());
        Assert.assertEquals(Notification.NotificationType.INFO, obtainedNotification.getType());
        Assert.assertEquals("tokens-service", obtainedNotification.getAffected());
        Assert.assertEquals("62721bd2-b29e-4776-af3f-ad6e95fdfe8a", obtainedNotification.getRef());
        Assert.assertEquals(Instant.ofEpochMilli(EPOCH_TIME), obtainedNotification.getCreatedAt());

    }

    @Test
    public void testThatNotificationStatusIsSet() throws Exception {
        notificationService.setNotificationStatus(USER_REF, NOTIFICATION_REF,
                                            true, Instant.ofEpochMilli(EPOCH_TIME));

        List<NotificationUser> notificationsUsers = notificationUserRepository.findAll();

        for(NotificationUser notificationUser : notificationsUsers) {
            if (notificationUser.getNotificationRef().equals(NOTIFICATION_REF)) {
                Assert.assertTrue(notificationUser.isRead());
                Assert.assertEquals(Instant.ofEpochMilli(EPOCH_TIME), notificationUser.getReadDate());
            }
        }
    }
}
