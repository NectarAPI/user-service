package ke.co.nectar.user.service;


import ke.co.nectar.user.NectarUserServiceApplication;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.Credentials;
import ke.co.nectar.user.entity.Permissions;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.repository.CredentialsRepository;
import ke.co.nectar.user.repository.PermissionsRepository;
import ke.co.nectar.user.repository.UserRepository;
import ke.co.nectar.user.service.credentials.CredentialsService;
import ke.co.nectar.user.service.credentials.impl.exceptions.InvalidCredentialsKey;
import ke.co.nectar.user.service.credentials.impl.exceptions.InvalidCredentialsRef;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarUserServiceApplication.class)
@FixMethodOrder(MethodSorters.JVM)
@Transactional
public class CredentialsServiceTest {

    @Autowired
    CredentialsService credentialsService;

    @Autowired
    CredentialsRepository credentialsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionsRepository permissionsRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        Permissions permissions = new Permissions("name", "identifier", "ref", "notes");
        permissions.setCreatedAt(Instant.now());
        List<Permissions> permissionList = new ArrayList<>();
        permissionList.add(permissions);

        User user = new User("firstName", "lastName", "test_username",
                "323232", "imageURL", "user_ref",
                "remember_token", "email@aa.com", "2121212", true);
        userRepository.save(user);

        Credentials credentials = new Credentials("key", "secret", "ref", true, permissionList, Instant.now(), Instant.now());
        credentials.setCreatedAt(Instant.now());
        credentials.setUser(user);

        credentialsRepository.save(credentials);
    }

    @Test
    public void findUserCredentialsByUserRefValidRequest() throws Exception {
        List<Credentials> credentials = credentialsService.getCredentialsByUserRef("user_ref");
        Assert.assertNotNull(credentials);
        Assert.assertEquals(1, credentials.size());
        Assert.assertEquals("ref", credentials.get(0).getRef());
    }

    @Test
    public void findUserCredentialsByRefValidRequest() throws Exception {
        Credentials credentials = credentialsService.getCredentials("ref");
        Assert.assertEquals("ref", credentials.getRef());
    }

    @Test
    public void findUserCredentialsByRefInvalidRequest() throws Exception {
        thrown.expect(InvalidCredentialsRef.class);
        thrown.expectMessage(equalTo(StringConstants.INVALID_MSG_CREDENTIAL_BY_REF));
        Credentials credentials = credentialsService.getCredentials("ref1");
    }

    @Test
    public void findUserCredentialsByKeyValidRequest() throws Exception {
        Credentials credentials = credentialsService.getCredentialsByKey("key");
        Assert.assertEquals("ref", credentials.getRef());
    }

    @Test
    public void findUserCredentialsByKeyInvalidRequest() throws Exception {
        thrown.expect(InvalidCredentialsKey.class);
        thrown.expectMessage(equalTo(StringConstants.INVALID_MSG_CREDENTIAL_BY_KEY));
        Credentials credentials = credentialsService.getCredentialsByKey("key1");
    }

    @Test
    public void addCredentialsValidRequest() throws Exception {
        Permissions permissions = new Permissions("name", "identifier", "ref", "notes");
        permissions.setCreatedAt(Instant.now());

        permissionsRepository.save(permissions);

        ArrayList<Long> permissionsIds = new ArrayList<>();
        permissionsIds.add(permissions.getId());

        Credentials credentials = credentialsService.addCredentials(permissionsIds, "user_ref");

        Assert.assertNotNull(credentials.getKey());
        Assert.assertNotNull(credentials.getSecret());
        Assert.assertNotNull(credentials.getRef());
        Assert.assertTrue(credentials.getActivated());
        Assert.assertEquals(1, credentials.getPermissions().size());
        Assert.assertEquals("name", credentials.getPermissions().get(0).getName());
        Assert.assertEquals("identifier", credentials.getPermissions().get(0).getIdentifier());
        Assert.assertEquals("ref", credentials.getPermissions().get(0).getRef());
        Assert.assertEquals("notes", credentials.getPermissions().get(0).getNotes());
        Assert.assertEquals("firstName", credentials.getUser().getFirstName());
        Assert.assertEquals("lastName", credentials.getUser().getLastName());
        Assert.assertEquals("test_username", credentials.getUser().getUsername());
        Assert.assertEquals("323232", credentials.getUser().getPhoneNumber());
        Assert.assertEquals("firstName", credentials.getUser().getFirstName());
        Assert.assertEquals("imageURL", credentials.getUser().getImageURL());
        Assert.assertEquals("user_ref", credentials.getUser().getRef());
        Assert.assertEquals("remember_token", credentials.getUser().getRememberToken());
        Assert.assertEquals("email@aa.com", credentials.getUser().getEmail());
        Assert.assertTrue(credentials.getUser().getActivated());
    }

    @Test
    public void deactivateCredentialsValidRequest() throws Exception {
        boolean deactivated = credentialsService.deactivateCredentials("ref", "user_ref");
        Assert.assertTrue(deactivated);
    }

    @Test
    public void deactivateCredentialsInvalidRequest() throws Exception {
        thrown.expect(InvalidCredentialsRef.class);
        thrown.expectMessage(equalTo(StringConstants.INVALID_MSG_CREDENTIAL_BY_REF));
        boolean deactivated = credentialsService.deactivateCredentials("ref1", "user_ref");
    }

    @Test
    public void activateCredentialsValidRequest() throws Exception {
        boolean activated = credentialsService.activateCredentials("ref", "user_ref");
        Assert.assertTrue(activated);
    }

    @Test
    public void activateCredentialsInvalidRequest() throws Exception {
        thrown.expect(InvalidCredentialsRef.class);
        thrown.expectMessage(equalTo(StringConstants.INVALID_MSG_CREDENTIAL_BY_REF));
        boolean activated = credentialsService.activateCredentials("ref1", "user_ref");
    }
}
