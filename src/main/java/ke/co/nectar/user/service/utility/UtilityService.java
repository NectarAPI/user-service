package ke.co.nectar.user.service.utility;

import ke.co.nectar.user.entity.Meter;
import ke.co.nectar.user.entity.Subscriber;
import ke.co.nectar.user.entity.UserUtility;
import ke.co.nectar.user.entity.Utility;

import java.util.List;

public interface  UtilityService {

    Utility add(Utility utility, String utilityRef) throws Exception;

    Utility find(String utilityRef) throws Exception;

    List<Utility> findAll() throws Exception;

    Utility findByRef(String ref) throws Exception;

    Utility update(Utility utility, String ref) throws Exception;

    boolean deactivateUtility(String ref, String userRef) throws Exception;

    boolean activateUtility(String ref, String userRef) throws Exception;

    List<UserUtility> getUserUtility(String userRef) throws Exception;

    List<Meter> getMeters(String ref) throws Exception;

    String setUserUtility(Utility utility, String userRef) throws Exception;
}
