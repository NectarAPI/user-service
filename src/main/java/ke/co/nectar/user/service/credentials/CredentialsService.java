package ke.co.nectar.user.service.credentials;

import ke.co.nectar.user.entity.Credentials;

import java.util.List;

public interface CredentialsService {

    List<Credentials> getCredentialsByUserRef(String userRef) throws Exception;

    Credentials getCredentials(String ref) throws Exception;

    Credentials getCredentialsByKey(String key) throws Exception;

    Credentials addCredentials(List<Long> permissionsIds, String userRef) throws Exception;

    boolean activateCredentials(String ref, String userRef) throws Exception;

    boolean deactivateCredentials(String ref, String userRef) throws Exception;
}
