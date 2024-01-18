package ke.co.nectar.user.service.user.impl;

import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.controllers.utils.ActivityLog;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.UserActivityCategory;
import ke.co.nectar.user.entity.UserActivityLog;
import ke.co.nectar.user.entity.Utility;
import ke.co.nectar.user.repository.UserActivityCategoryRepository;
import ke.co.nectar.user.repository.UserActivityLogRepository;
import ke.co.nectar.user.repository.UserRepository;
import ke.co.nectar.user.service.user.UserService;
import ke.co.nectar.user.service.user.impl.exceptions.*;
import ke.co.nectar.user.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivityLogRepository userActivityLogRepository;

    @Autowired
    private UserActivityCategoryRepository userActivityCategoryRepository;

    @Override
    public String add(User user, String userRef) throws Exception {
        if (user.validate()) {
            if (userRepository.findByRef(user.getRef()) != null) {
                user.setUpdatedAt(Instant.now());
                userRepository.save(user);
                return user.getRef();
            } else {

                if (userRepository.existsByEmail(user.getEmail())) {
                    throw new EmailExistsException(StringConstants.EMAIL_ALREADY_EXIST_BY_USER);

                } else if (userRepository.existsByUsername(user.getUsername())) {
                    throw new UsernameExistsException(StringConstants.USER_NAME_ALREADY_EXIST_BY_USER);

                } else if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
                    throw new PhoneNoExistsException(StringConstants.PHONE_NO_ALREADY_EXIST_BY_USER);

                } else {
                    if (user.getRef() == null) {
                        String addedUserRef = AppUtils.generateRef();
                        user.setRef(addedUserRef);
                    }
                    user.setCreatedAt(Instant.now());
                    userRepository.save(user);
                    return user.getRef();
                }
            }
        }
        throw new InvalidUserDetailsException(StringConstants.INVALID_USER_DETAILS);
    }

    @Override
    public User find(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new InvalidUsernameException(StringConstants.INVALID_USER_NAME);
    }

    @Override
    public List<User> findAll() throws Exception {
        return userRepository.findAll();
    }

    @Override
    public User findByRef(String ref) throws Exception {
        User user = userRepository.findByRef(ref);
        if (user != null) {
            return user;
        }
        throw new InvalidUserRefException(String.format(StringConstants.INVALID_USER_REF_EXCEPTION, ref));
    }

    @Override
    public User findByRefAndRememberToken(String ref, String rememberToken) throws Exception {
        User user = userRepository.findByRefAndRememberToken(ref, rememberToken);
        if (user != null) {
            return user;
        }
        throw new InvalidUserRefRememberTokenException(String.format(StringConstants.INVALID_USER_REF_REMEMBER_TOKEN_EXCEPTION, ref, rememberToken));
    }

    @Override
    public User findByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,24}$")) {
            if (user != null) {
                return user;
            }
        }
        throw new InvalidEmailException(String.format(StringConstants.INVALID_EMAIL_EXCEPTION, email));
    }

    @Override
    public List<Utility> getUserUtilities(String userRef) throws Exception {
        User user = userRepository.findByRef(userRef);
        if (user != null) {
            return user.getUtilities();
        }
        throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
    }

    @Override
    public void update(User user, String ref) throws Exception {
        if (userRepository.findByRef(ref) != null) {
            userRepository.save(user);
        } else {
            throw new InvalidUserRefException(StringConstants.INVALID_USER_NAME);
        }
    }

    @Override
    public boolean updateUserRememberToken(String rememberToken, String ref)
            throws Exception {
        if (userRepository.findByRef(ref) != null) {
            return userRepository.updateUserRememberToken(rememberToken, ref) > 0;
        } else {
            throw new InvalidUsernameException(StringConstants.INVALID_USER_REF);
        }
    }

    @Override
    public boolean updateUserPassword(String ref, String password) throws Exception {
        if (userRepository.findByRef(ref) != null) {
            return userRepository.updatePassword(new BCryptPasswordEncoder().encode(password), ref) > 0;
        } else {
            throw new InvalidUsernameException(StringConstants.INVALID_USER_REF);
        }
    }

    @Override
    public boolean updateEmailVerifiedAt(Instant emailVerifiedAt, String ref)
            throws Exception {
        if (userRepository.findByRef(ref) != null) {
            return userRepository.updateEmailVerifiedAt(emailVerifiedAt, ref) > 0;
        } else {
            throw new InvalidUsernameException(StringConstants.INVALID_USER_REF);
        }
    }

    @Override
    public boolean deactivateUser(String ref) {
        return userRepository.deactivateUser(ref) > 0;
    }

    @Override
    public List<UserActivityLog> getUserActivityLogs(String userRef) throws Exception {
        return userActivityLogRepository.findByUserOrderByCreatedAtDesc(userRepository.findByRef(userRef));
    }

    @Override
    public String setUserActivityLog(ActivityLog userActivityLog,
                                     String userRef) throws Exception {
        return userActivityLogRepository.save(generateUserActivityLog(userActivityLog,
                userRef)).getRef();
    }

    private UserActivityLog generateUserActivityLog(ActivityLog activityLog,
                                                    String userRef)
            throws InvalidUserRefException, InvalidUserActivityCategory {
        User user =  userRepository.findByRef(userRef);
        UserActivityCategory category = userActivityCategoryRepository.findByName(activityLog.getCategory());

        if (user != null) {
            if (category != null) {
                UserActivityLog userActivityLog = new UserActivityLog();
                userActivityLog.setRef(AppUtils.generateRef());
                userActivityLog.setCategory(category);
                userActivityLog.setDescription(activityLog.getDescription());
                userActivityLog.setUser(user);
                userActivityLog.setCreatedAt(Instant.now());
                userActivityLog.setUpdatedAt(Instant.now());
                return userActivityLog;
            }
            throw new InvalidUserActivityCategory(
                    String.format("%s %s",
                            StringConstants.INVALID_USER_ACTIVITY_CATEGORY,
                            activityLog.getCategory())
            );
        }
        throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);

    }
}
