package ke.co.nectar.user.service.subscriber.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.Subscriber;
import ke.co.nectar.user.entity.SubscriberUtility;
import ke.co.nectar.user.entity.Utility;
import ke.co.nectar.user.repository.SubscriberMetersRepository;
import ke.co.nectar.user.repository.SubscriberRepository;
import ke.co.nectar.user.repository.SubscriberUtilityRepository;
import ke.co.nectar.user.repository.UtilityRepository;
import ke.co.nectar.user.service.meters.MetersService;
import ke.co.nectar.user.service.subscriber.SubscriberService;
import ke.co.nectar.user.service.subscriber.impl.exceptions.InvalidSubscriberDetailsException;
import ke.co.nectar.user.service.subscriber.impl.exceptions.InvalidSubscriberNameException;
import ke.co.nectar.user.service.subscriber.impl.exceptions.InvalidSubscriberRefException;
import ke.co.nectar.user.service.utility.impl.exceptions.InvalidUtilityRefException;
import ke.co.nectar.user.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private MetersService metersService;

    @Autowired
    private SubscriberMetersRepository subscriberMetersRepository;

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private SubscriberUtilityRepository subscriberUtilityRepository;

    @Override
    public Subscriber add(SubscriberBundle subscriberBundle, String userRef) throws Exception {
        Subscriber subscriber = processSubscriberBundle(new Subscriber(), subscriberBundle);
        if (subscriber.validate()) {
            subscriberRepository.save(subscriber);
            Utility utility = utilityRepository.findByRef(subscriberBundle.getUtilityRef());
            if (utility != null) {
                SubscriberUtility subscriberUtility = new SubscriberUtility();
                subscriberUtility.setSubscriber(subscriber);
                subscriberUtility.setUtility(utility);
                subscriberUtilityRepository.save(subscriberUtility);
            } else {
                throw new InvalidUtilityRefException(StringConstants.INVALID_UTILITY_REF);
            }

            return subscriber;
        }
        throw new InvalidSubscriberDetailsException(StringConstants.INVALID_SUBSCRIBER_DETAILS);
    }

    @Override
    public Subscriber find(String subscriberName) throws Exception {
        Subscriber subscriber = subscriberRepository.findByName(subscriberName);
        if (subscriber != null) {
            return subscriber;
        }
        throw new InvalidSubscriberNameException(StringConstants.INVALID_SUBSCRIBER_NAME);
    }

    @Override
    public List<Subscriber> findAll() throws Exception {
        return subscriberRepository.findAll();
    }

    @Override
    public Subscriber findByRef(String ref) throws Exception {
        Subscriber subscriber = subscriberRepository.findByRef(ref);
        if (subscriber != null) {
            return subscriber;
        }
        throw new InvalidSubscriberRefException(String.format(StringConstants.INVALID_SUBSCRIBER_REF_EXCEPTION, ref));
    }

    @Override
    public SubscriberUtility findByRefAndUtility(String subscriberRef, String utilityRef) throws Exception {
        Subscriber subscriber = subscriberRepository.findByRef(subscriberRef);
        if  (subscriber != null) {
            Utility utility = utilityRepository.findByRef(utilityRef);
            if (utility != null) {
                return subscriberUtilityRepository.findBySubscriberAndUtility(subscriber, utility);
            }
            throw new InvalidUtilityRefException(StringConstants.INVALID_UTILITY_REF);
        }
        throw new InvalidSubscriberRefException(StringConstants.INVALID_SUBSCRIBER_REF_EXCEPTION);

    }

    @Override
    public boolean activateSubscriber(String subscriberRef) throws Exception {
        return subscriberRepository.activateSubscriber(subscriberRef) > 0;
    }

    @Override
    public boolean deactivateSubscriber(String subscriberRef) throws Exception {
        return subscriberRepository.deactivateSubscriber(subscriberRef) > 0;
    }

    @Override
    public Subscriber update(SubscriberBundle subscriberBundle, String subscriberRef, String userRef) throws Exception {
        Subscriber subscriber = subscriberRepository.findByRef(subscriberRef);
        if (subscriber != null) {
            Subscriber obtainedSubscriber = processSubscriberBundle(subscriber, subscriberBundle);
            subscriber.setActivated(obtainedSubscriber.isActivated());
            subscriber.setName(obtainedSubscriber.getName());
            subscriber.setPhoneNo(obtainedSubscriber.getPhoneNo());
            subscriber.setCreatedAt(obtainedSubscriber.getCreatedAt());
            subscriber.setUpdatedAt(obtainedSubscriber.getUpdatedAt());
            subscriberRepository.save(subscriber);
            return subscriber;
        } else {
            throw new InvalidSubscriberRefException(StringConstants.INVALID_SUBSCRIBER_REF);
        }
    }

    private Subscriber processSubscriberBundle(Subscriber subscriber, SubscriberBundle subscriberBundle)
        throws Exception {
        subscriber.setName(subscriberBundle.getName());
        subscriber.setActivated(subscriberBundle.isActivated());
        subscriber.setPhoneNo(subscriberBundle.getPhoneNo());
        subscriber.setRef(subscriber.getRef() == null ? AppUtils.generateRef() : subscriber.getRef());
        subscriber.setCreatedAt(subscriber.getCreatedAt() == null ? Instant.now() : subscriber.getCreatedAt());
        subscriber.setUpdatedAt(Instant.now());
        return subscriber;
    }

    public static class SubscriberBundle {

        private String name;

        private boolean activated;

        @JsonProperty("phone_no")
        private String phoneNo;

        @JsonProperty("utility")
        private String utilityRef;

        public SubscriberBundle() {}

        public SubscriberBundle(String name, String phoneNo,
                                boolean activated, String utilityRef) {
            setName(name);
            setPhoneNo(phoneNo);
            setActivated(activated);
            setUtilityRef(utilityRef);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public boolean isActivated() {
            return activated;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }

        public String getUtilityRef() {
            return utilityRef;
        }

        public void setUtilityRef(String utilityRef) {
            this.utilityRef = utilityRef;
        }
    }
}
