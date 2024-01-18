package ke.co.nectar.user.service.permissions;

import ke.co.nectar.user.entity.Permissions;

import java.util.List;

public interface PermissionsService {

    List<Permissions> getPermissions() throws Exception;
}
