package ke.co.nectar.user.service;

import ke.co.nectar.user.NectarUserServiceApplication;
import ke.co.nectar.user.entity.Permissions;
import ke.co.nectar.user.repository.PermissionsRepository;
import ke.co.nectar.user.service.permissions.PermissionsService;
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
public class PermissionsServiceTest {

    @Autowired
    PermissionsService permissionsService;

    @Autowired
    PermissionsRepository permissionsRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Permissions permissions;

    @Before
    public void setup() {
        final long EPOCH_TIME = 1606754076302l;
        permissions = new Permissions("permissions_name", "permissions_identifier",
                                        "permissions_ref", "permissions_notes");
        permissions.setCreatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        permissions.setUpdatedAt(Instant.ofEpochMilli(EPOCH_TIME));
        permissionsRepository.save(permissions);
    }

    @Test
    public void findPermissions() throws Exception {
        List<Permissions> obtainedPermissions = permissionsService.getPermissions();
        int size = obtainedPermissions.size() - 1;
        Assert.assertNotNull(obtainedPermissions);
        Assert.assertTrue(obtainedPermissions.size() > 0);
        Assert.assertEquals("permissions_name", obtainedPermissions.get(size).getName());
        Assert.assertEquals("permissions_identifier", obtainedPermissions.get(size).getIdentifier());
        Assert.assertEquals("permissions_ref", obtainedPermissions.get(size).getRef());
        Assert.assertEquals("permissions_notes", obtainedPermissions.get(size).getNotes());
    }
}
