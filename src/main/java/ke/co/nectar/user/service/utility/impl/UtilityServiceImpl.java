package ke.co.nectar.user.service.utility.impl;

import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.*;
import ke.co.nectar.user.repository.UserRepository;
import ke.co.nectar.user.repository.UserUtilityRepository;
import ke.co.nectar.user.repository.UtilityRepository;
import ke.co.nectar.user.service.user.UserService;
import ke.co.nectar.user.service.user.impl.exceptions.InvalidUserRefException;
import ke.co.nectar.user.service.utility.UtilityService;
import ke.co.nectar.user.service.utility.impl.exceptions.InvalidUtilityDetailsException;
import ke.co.nectar.user.service.utility.impl.exceptions.InvalidUtilityException;
import ke.co.nectar.user.service.utility.impl.exceptions.InvalidUtilityRefException;
import ke.co.nectar.user.service.utility.impl.exceptions.NameExistsException;
import ke.co.nectar.user.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;

@Service
public class UtilityServiceImpl implements UtilityService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtilityRepository userUtilityRepository;

    @Override
    public Utility add(Utility utility, String userRef) throws Exception {
        if (utility.validate()) {
            if (utilityRepository.findByRef(utility.getRef()) != null) {
                utility.setUpdatedAt(Instant.now());
                utilityRepository.save(utility);
                return utility;
            } else {
                if (utilityRepository.existsByName(utility.getName())) {
                    throw new NameExistsException(StringConstants.UTILITY_NAME_EXISTS);
                } else {
                    User user = userService.findByRef(userRef);
                    if (user != null) {
                        String addedUtilityRef = AppUtils.generateRef();
                        utility.setRef(addedUtilityRef);
                        utility.setCreatedAt(Instant.now());
                        utility.setUpdatedAt(Instant.now());
                        utilityRepository.save(utility);
                        setUserUtility(utility, userRef);
                        return utility;
                    }
                    throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
                }
            }
        }
        throw new InvalidUtilityDetailsException(StringConstants.INVALID_UTILITY_DETAILS);
    }

    public Utility find(String utilityRef) throws Exception {
        Utility utility = utilityRepository.findByRef(utilityRef);
        if (utility != null) {
            return utility;
        }
        throw new InvalidUtilityException(StringConstants.INVALID_UTILITY_REF);
    }

    @Override
    public List<Utility> findAll() throws Exception {
        return utilityRepository.findAll();
    }

    @Override
    public Utility findByRef(String ref) throws Exception {
        Utility utility = utilityRepository.findByRef(ref);
        if (utility != null) {
            return utility;
        }
        throw new InvalidUtilityRefException(String.format(StringConstants.INVALID_UTILITY_REF_EXCEPTION, ref));
    }

    @Override
    public Utility update(Utility utility, String utilityRef) throws Exception {
        Utility obtainedUtility = findByRef(utilityRef);
        if (obtainedUtility != null) {
            obtainedUtility.setName(utility.getName());
            obtainedUtility.setContactPhoneNo(utility.getContactPhoneNo());
            obtainedUtility.setActivated(utility.getActivated());
            obtainedUtility.setUnitCharge(utility.getUnitCharge());
            obtainedUtility.setUpdatedAt(Instant.now());
            obtainedUtility.setConfigRef(utility.getConfigRef());
            utilityRepository.save(obtainedUtility);
            return obtainedUtility;
        } else {
            throw new InvalidUtilityRefException(utilityRef);
        }
    }

    @Override
    public boolean activateUtility(String ref, String userRef) throws Exception {
        return utilityRepository.activateUtility(ref) > 0;
    }

    @Override
    public boolean deactivateUtility(String ref, String userRef) throws Exception {
        return utilityRepository.deactivateUtility(ref) > 0;
    }

    @Override
    public List<UserUtility> getUserUtility(String userRef) throws Exception {
        return userUtilityRepository.findByUserOrderByCreatedAtDesc(userRepository.findByRef(userRef));
    }

    @Override
    public List<Meter> getMeters(String ref) throws Exception {
        Utility utility = utilityRepository.findByRef(ref);
        if (utility != null) {
            return utility.getMeters();
        }
        throw new InvalidUtilityRefException(StringConstants.INVALID_UTILITY_REF);
    }

    @Override
    public String setUserUtility(Utility utility,
                                 String userRef) throws Exception {
        return userUtilityRepository.save(generateUserUtility(utility,
                userRef)).getRef();
    }

    private UserUtility generateUserUtility(Utility utility,
                                            String userRef) throws Exception {
        User user = userRepository.findByRef(userRef);
        if (user != null) {
            UserUtility userUtility = new UserUtility();
            userUtility.setUser(user);
            userUtility.setUtility(utility);
            userUtility.setRef(AppUtils.generateRef());
            userUtility.setCreatedAt(Instant.now());
            userUtility.setUpdatedAt(Instant.now());
            return userUtility;
        }
        throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
    }
}
