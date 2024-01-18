package ke.co.nectar.user.service;

import ke.co.nectar.user.NectarUserServiceApplication;
import ke.co.nectar.user.controllers.utils.ActivityLog;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.UserActivityCategory;
import ke.co.nectar.user.entity.UserActivityLog;
import ke.co.nectar.user.repository.UserActivityCategoryRepository;
import ke.co.nectar.user.repository.UserActivityLogRepository;
import ke.co.nectar.user.repository.UserRepository;
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
public class UsersServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserActivityCategoryRepository userActivityCategoryRepository;

    @Autowired
    UserActivityLogRepository userActivityLogRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;

    private final long EPOCH_TIME = 1606754076302L;

    @Before
    public void setup() {
        user = new User("firstName", "lastName", "test_username",
                        "323232", "imageURL", "f249dfea-f1b2-4145-b848-6d2004f112d6",
                        "remember_token", "email@aa.com", "2121212", true);
        userRepository.save(user);

        UserActivityCategory userActivityCategory = new UserActivityCategory("LOGIN",
                "9e0f044e-7263-47fa-8b23-a4904f050eb0",
                "User Login Activity",
                Instant.ofEpochMilli(EPOCH_TIME),
                Instant.ofEpochMilli(EPOCH_TIME));
        userActivityCategoryRepository.save(userActivityCategory);

        UserActivityLog userActivityLog = new UserActivityLog(user, "6a0c64bb-bbbe-4a8c-86f4-1a94dc937e6e", userActivityCategory,
                "Logged In",
                Instant.ofEpochMilli(EPOCH_TIME), Instant.ofEpochMilli(EPOCH_TIME));
        userActivityLogRepository.save(userActivityLog);
    }

    @Test
    public void saveUser() throws Exception {
        final String CREATE_USER_REF = "f249dfea-f1b2-4145-b848-6d2004f112d6";
        User user = new User("anotherFirstName", "anotherLastName", "anotherUsername",
                        "0323232", "https://anotherimage.com",
                                "630a5940-5d2f-48c0-bafa-27d276107091", "remember_token",
                              "another-email@aa.com", "2121212", true);
        String ref = userService.add(user, CREATE_USER_REF);
        User updatedUser = userService.find("anotherUsername");

        Assert.assertNotNull(ref);
        Assert.assertEquals("anotherFirstName", updatedUser.getFirstName());
        Assert.assertEquals("anotherLastName", updatedUser.getLastName());
        Assert.assertEquals("anotherUsername", updatedUser.getUsername());
        Assert.assertEquals("0323232", updatedUser.getPhoneNumber());
        Assert.assertEquals("https://anotherimage.com", updatedUser.getImageURL());
        Assert.assertEquals("630a5940-5d2f-48c0-bafa-27d276107091", updatedUser.getRef());
        Assert.assertEquals("remember_token", updatedUser.getRememberToken());
        Assert.assertEquals("another-email@aa.com", updatedUser.getEmail());
        Assert.assertTrue(updatedUser.getActivated());
    }

    @Test
    public void saveUserWithoutRef() throws Exception {
        final String CREATE_USER_REF = "f249dfea-f1b2-4145-b848-6d2004f112d6";
        User user = new User("anotherFirstName", "anotherLastName", "anotherUsername",
                "0323232", "https://anotherimage.com", "remember_token",
                "another-email@aa.com", "2121212", true);
        String ref = userService.add(user, CREATE_USER_REF);
        User updatedUser = userService.find("anotherUsername");

        Assert.assertNotNull(ref);
        Assert.assertEquals("anotherFirstName", updatedUser.getFirstName());
        Assert.assertEquals("anotherLastName", updatedUser.getLastName());
        Assert.assertEquals("anotherUsername", updatedUser.getUsername());
        Assert.assertEquals("0323232", updatedUser.getPhoneNumber());
        Assert.assertEquals("https://anotherimage.com", updatedUser.getImageURL());
        Assert.assertEquals("remember_token", updatedUser.getRememberToken());
        Assert.assertEquals("another-email@aa.com", updatedUser.getEmail());
        Assert.assertTrue(updatedUser.getActivated());
    }

    @Test
    public void findUser() throws Exception {
        User user = userService.find("test_username");
        Assert.assertEquals("firstName", user.getFirstName());
    }

    @Test
    public void findUserByRef() throws Exception {
        User user = userService.findByRef("f249dfea-f1b2-4145-b848-6d2004f112d6");
        Assert.assertEquals("firstName", user.getFirstName());
    }

    @Test
    public void findByRefAndRememberToken() throws Exception {
        User user = userService.findByRefAndRememberToken("f249dfea-f1b2-4145-b848-6d2004f112d6", "remember_token");
        Assert.assertEquals("firstName", user.getFirstName());
    }

    @Test
    public void updateUser() throws Exception {
        User user = new User("firstName111", "lastName111", "updated_username",
                "100323232", "imageURL", "updated_ref", "remember_token",
                      "email@aa.com", "2121212", false);

        userService.update(user, "f249dfea-f1b2-4145-b848-6d2004f112d6");

        User updatedUser = userService.findByRef("updated_ref");
        Assert.assertEquals("firstName111", updatedUser.getFirstName());
        Assert.assertEquals("lastName111", updatedUser.getLastName());
        Assert.assertEquals("updated_username", updatedUser.getUsername());
        Assert.assertEquals("100323232", updatedUser.getPhoneNumber());
        Assert.assertEquals("imageURL", updatedUser.getImageURL());
        Assert.assertEquals("updated_ref", updatedUser.getRef());
        Assert.assertEquals("remember_token", updatedUser.getRememberToken());
        Assert.assertEquals("email@aa.com", updatedUser.getEmail());
        Assert.assertFalse(updatedUser.getActivated());
    }

    @Test
    public void updateUserRememberToken() throws Exception {
        final String UPDATED_REMEMBER_TOKEN = "remember_token_updated";

        userService.updateUserRememberToken(UPDATED_REMEMBER_TOKEN, "f249dfea-f1b2-4145-b848-6d2004f112d6");
        User updatedUser = userService.find(user.getUsername());
        Assert.assertEquals(UPDATED_REMEMBER_TOKEN, updatedUser.getRememberToken());
    }

    @Test
    public void updateUserPassword() throws Exception {
        final String UPDATED_PASSWORD = "new_password";

        userService.updateUserPassword("f249dfea-f1b2-4145-b848-6d2004f112d6", UPDATED_PASSWORD);
        User updatedUser = userService.find(user.getUsername());
        Assert.assertNotNull(updatedUser.getPassword());
        Assert.assertNotEquals(user.getPassword(), updatedUser.getPassword());
    }

    @Test
    public void deleteUser() throws Exception {
        boolean deleted = userService.deactivateUser("f249dfea-f1b2-4145-b848-6d2004f112d6");
        Assert.assertTrue(deleted);
    }

    @Test
    public void testThatUserActivityLogIsReturned() throws Exception {
        List<UserActivityLog> activityLogList = userService.getUserActivityLogs("f249dfea-f1b2-4145-b848-6d2004f112d6");

        Assert.assertEquals(1, activityLogList.size());
        Assert.assertEquals("6a0c64bb-bbbe-4a8c-86f4-1a94dc937e6e", activityLogList.get(0).getRef());

        Assert.assertEquals("LOGIN", activityLogList.get(0).getCategory().getName());
        Assert.assertEquals("9e0f044e-7263-47fa-8b23-a4904f050eb0", activityLogList.get(0).getCategory().getRef());
        Assert.assertEquals("User Login Activity", activityLogList.get(0).getCategory().getNotes());
        Assert.assertEquals(Instant.ofEpochMilli(EPOCH_TIME), activityLogList.get(0).getCategory().getCreatedAt());
        Assert.assertEquals(Instant.ofEpochMilli(EPOCH_TIME), activityLogList.get(0).getCategory().getUpdatedAt());

        Assert.assertEquals("Logged In", activityLogList.get(0).getDescription());
        Assert.assertEquals(Instant.ofEpochMilli(EPOCH_TIME), activityLogList.get(0).getCreatedAt());
        Assert.assertEquals(Instant.ofEpochMilli(EPOCH_TIME), activityLogList.get(0).getUpdatedAt());

        Assert.assertEquals("firstName", activityLogList.get(0).getUser().getFirstName());
        Assert.assertEquals("lastName", activityLogList.get(0).getUser().getLastName());
    }

    @Test
    public void testThatUserActivityLogIsSaved() throws Exception {
        User user = new User("firstName111", "lastName111", "updated_username",
                "100323232", "imageURL", "ref", "remember_token",
                "email@aa.com", "2121212", false);
        userRepository.save(user);

        ActivityLog activityLog = new ActivityLog("LOGIN", "User Logged in");
        userService.setUserActivityLog(activityLog, "ref");

        List<UserActivityLog> activityLogList = userService.getUserActivityLogs("ref");

        Assert.assertEquals(1, activityLogList.size());
        Assert.assertNotNull(activityLogList.get(0).getRef());
        Assert.assertEquals("LOGIN", activityLogList.get(0).getCategory().getName());
        Assert.assertEquals("User Login Activity", activityLogList.get(0).getCategory().getNotes());
        Assert.assertNotNull(activityLogList.get(0).getCreatedAt());
        Assert.assertNotNull(activityLogList.get(0).getUpdatedAt());
    }
}
