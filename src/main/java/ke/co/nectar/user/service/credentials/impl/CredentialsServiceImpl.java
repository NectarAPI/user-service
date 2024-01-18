package ke.co.nectar.user.service.credentials.impl;

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
import ke.co.nectar.user.service.credentials.impl.exceptions.InvalidPermissionsIds;
import ke.co.nectar.user.service.credentials.impl.exceptions.InvalidUserException;
import ke.co.nectar.user.service.user.impl.exceptions.InvalidUserRefException;
import ke.co.nectar.user.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;

@Service
public class CredentialsServiceImpl implements CredentialsService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Credentials getCredentials(String ref) throws Exception {
        Credentials credentials = credentialsRepository.findByRef(ref);
        if (credentials != null) {
            return credentials;
        } else {
            throw new InvalidCredentialsRef(StringConstants.INVALID_MSG_CREDENTIAL_BY_REF);
        }
    }

    @Override
    public Credentials getCredentialsByKey(String key) throws Exception{
        Credentials credentials = credentialsRepository.findByKey(key);
        if (credentials != null) {
            return credentials;
        } else {
            throw new InvalidCredentialsKey(StringConstants.INVALID_MSG_CREDENTIAL_BY_KEY);
        }
    }

    @Override
    public List<Credentials> getCredentialsByUserRef(String userRef) throws Exception {
        List<Credentials> credentials = credentialsRepository.findByUserRef(userRef);
        if (credentials != null) {
            return credentials;
        } else {
            throw new InvalidCredentialsRef(StringConstants.INVALID_MSG_CREDENTIAL_BY_USER_REF);
        }
    }

    @Override
    public Credentials addCredentials(List<Long> permissionsIds, String userRef) throws Exception {
        if (userRef != null) {
            User user = userRepository.findByRef(userRef);

            if (user != null) {
                if (permissionsIds != null && permissionsIds.size() > 0) {
                    List<Permissions> permissions = permissionsRepository.findAllById(permissionsIds);
                    Credentials credentials = new Credentials();
                    credentials.setKey(AppUtils.generateRef());
                    credentials.setSecret(AppUtils.generateRef());
                    credentials.setRef(AppUtils.generateRef());
                    credentials.setUser(user);
                    credentials.setActivated(true);
                    credentials.setCreatedAt(Instant.now());
                    credentials.setPermissions(permissions);
                    credentialsRepository.save(credentials);
                    return credentials;
                }
                throw new InvalidPermissionsIds(StringConstants.INVALID_PERMISSIONS_IDS);
            }

            throw new InvalidUserException(StringConstants.INVALID_USER);
        }

        throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
    }

    @Override
    public boolean activateCredentials(String ref, String userRef) throws Exception {
        if (getCredentials(ref).getUser().getRef().equals(userRef))
            return credentialsRepository.activateCredentials(ref) > 0;
        throw new InvalidUserException(StringConstants.INVALID_MSG_CREDENTIAL_BY_USER_REF);
    }

    @Override
    public boolean deactivateCredentials(String ref, String userRef) throws Exception {
        if (getCredentials(ref).getUser().getRef().equals(userRef))
            return credentialsRepository.deactivateCredentials(ref) > 0;
        throw new InvalidUserException(StringConstants.INVALID_MSG_CREDENTIAL_BY_USER_REF);
    }
}
