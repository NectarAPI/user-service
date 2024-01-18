package ke.co.nectar.user.service.user;

import ke.co.nectar.user.controllers.utils.ActivityLog;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.UserActivityLog;
import ke.co.nectar.user.entity.Utility;

import java.time.Instant;
import java.util.List;

public interface UserService {

    String add(User user, String userRef) throws Exception;

    User find(String userName) throws Exception;

    List<User> findAll() throws Exception;

    User findByRef(String ref) throws Exception;

    User findByEmail(String email) throws Exception;

    User findByRefAndRememberToken(String ref, String rememberToken) throws Exception;

    boolean updateUserPassword(String ref, String password) throws Exception;

    List<Utility> getUserUtilities(String userRef) throws Exception;

    void update(User user, String ref) throws Exception;

    boolean updateUserRememberToken(String rememberToken, String ref) throws Exception;

    boolean updateEmailVerifiedAt(Instant emailVerifiedAt, String ref) throws Exception;

    boolean deactivateUser(String ref) throws Exception;

    List<UserActivityLog> getUserActivityLogs(String userRef) throws Exception;

    String setUserActivityLog(ActivityLog userActivityLog, String userRef) throws Exception;

}
