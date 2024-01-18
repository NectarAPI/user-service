package ke.co.nectar.user.service.meters.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.*;
import ke.co.nectar.user.repository.*;
import ke.co.nectar.user.service.meters.MetersService;
import ke.co.nectar.user.service.meters.impl.exceptions.InvalidMeterDetailsException;
import ke.co.nectar.user.service.meters.impl.exceptions.InvalidMeterRefException;
import ke.co.nectar.user.service.meters.impl.exceptions.InvalidMeterTypeException;
import ke.co.nectar.user.service.meters.impl.exceptions.MeterExistsException;
import ke.co.nectar.user.service.subscriber.impl.exceptions.InvalidSubscriberRefException;
import ke.co.nectar.user.service.utility.impl.exceptions.InvalidUtilityRefException;
import ke.co.nectar.user.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class MetersServiceImpl implements MetersService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private MeterTypeRepository meterTypeRepository;

    @Autowired
    private UtilityMetersRepository utilityMetersRepository;

    @Autowired
    private SubscriberMetersRepository subscriberMetersRepository;

    @Override
    public CompositeMeterBundle add(MeterBundle meterBundle, String userRef) throws Exception {
        Meter meter = processMeterBundle(new Meter(), meterBundle);
        CompositeMeterBundle compositeMeterBundle = new CompositeMeterBundle();
        if (meter.validate()) {

            if (meterRepository.existsByNo(meter.getNo())) {
                throw new MeterExistsException(StringConstants.METER_ALREADY_EXISTS);
            } else {
                compositeMeterBundle.setRef(meter.getRef());
                compositeMeterBundle.setNo(meter.getNo());
                compositeMeterBundle.setActivated(meter.getActivated());
                compositeMeterBundle.setMeterType(meter.getMeterType());
                compositeMeterBundle.setCreatedAt(meter.getCreatedAt());
                compositeMeterBundle.setUpdatedAt(meter.getUpdatedAt());

                Utility utility = utilityRepository.findByRef(meterBundle.getUtilityRef());
                if (utility != null) {
                    compositeMeterBundle.setUtility(utility);
                } else {
                    throw new InvalidUtilityRefException(StringConstants.INVALID_UTILITY_REF);
                }

                if (meterBundle.getSubscriberRef() != null) {
                    Subscriber subscriber = subscriberRepository.findByRef(meterBundle.getSubscriberRef());
                    if (subscriber != null) {
                        meter.setSubscriber(subscriber);
                        compositeMeterBundle.setSubscriber(subscriber);
                        meterRepository.save(meter);
                        SubscriberMeter subscriberMeters = new SubscriberMeter(meter, subscriber,
                                                                                Instant.now(), Instant.now());
                        subscriberMetersRepository.save(subscriberMeters);
                    } else {
                        throw new InvalidSubscriberRefException(StringConstants.INVALID_SUBSCRIBER_REF_EXCEPTION);
                    }
                } else {
                    meterRepository.save(meter);
                }

                return compositeMeterBundle;
            }
        }
        throw new InvalidMeterDetailsException(StringConstants.INVALID_METER_DETAILS);
    }

    @Override
    public Meter findByRef(String ref) throws Exception {
        Meter meter = meterRepository.findByRef(ref);
        if (meter != null) {
            return meter;
        }
        throw new InvalidMeterRefException(String.format(StringConstants.INVALID_METER_REF_EXCEPTION, ref));
    }

    @Override
    public List<MeterType> getMeterTypes() throws Exception {
        return meterTypeRepository.findAll();
    }

    @Override
    public CompositeMeterBundle update(MeterBundle meterBundle, String meterRef, String userRef) throws Exception {
        Meter meter = meterRepository.findByRef(meterRef);
        CompositeMeterBundle compositeMeterBundle = new CompositeMeterBundle();
        if (meter != null) {
            Meter obtainedMeter = processMeterBundle(meter, meterBundle);

            compositeMeterBundle.setNo(obtainedMeter.getNo());
            compositeMeterBundle.setRef(obtainedMeter.getRef());
            compositeMeterBundle.setActivated(obtainedMeter.getActivated());
            compositeMeterBundle.setMeterType(obtainedMeter.getMeterType());

            Utility utility = utilityRepository.findByRef(meterBundle.getUtilityRef());
            if (utility != null) {
                UtilityMeters utilityMeters = utilityMetersRepository.findByMeter(meter);
                if (utilityMeters == null) {
                    utilityMeters = new UtilityMeters(meter, utility, Instant.now(), Instant.now());
                } else {
                    utilityMeters.setUtility(utility);
                }
                utilityMetersRepository.save(utilityMeters);
                compositeMeterBundle.setUtility(utility);

            } else {
                throw new InvalidUtilityRefException(StringConstants.INVALID_UTILITY_REF);
            }

            if (meterBundle.getSubscriberRef() != null) {
                Subscriber subscriber = subscriberRepository.findByRef(meterBundle.getSubscriberRef());
                if (subscriber != null) {
                    meter.setSubscriber(subscriber);
                    compositeMeterBundle.setSubscriber(subscriber);
                    meterRepository.save(meter);
                    SubscriberMeter subscriberMeters = subscriberMetersRepository.findBySubscriberAndMeter(subscriber, meter);
                    subscriberMeters.setSubscriber(subscriber);
                    subscriberMetersRepository.save(subscriberMeters);
                } else {
                    throw new InvalidSubscriberRefException(StringConstants.INVALID_SUBSCRIBER_REF_EXCEPTION);
                }
            } else {
                meterRepository.save(meter);
            }

        } else {
            throw new InvalidMeterRefException(StringConstants.INVALID_METER_REF);
        }
        return compositeMeterBundle;
    }

    @Override
    public boolean activateMeter(String ref, String userRef) throws Exception {
        return meterRepository.activateMeter(ref) > 0;
    }

    @Override
    public boolean deactivateMeter(String ref, String userRef) throws Exception {
        return meterRepository.deactivateMeter(ref) > 0;
    }

    public Meter processMeterBundle(Meter meter, MeterBundle meterBundle) throws Exception {
        MeterType meterType = meterTypeRepository.findByRef(meterBundle.getMeterType());
        if (meterType != null) {
            meter.setMeterType(meterType);
            meter.setNo(meterBundle.getNo());
            meter.setActivated(meterBundle.getActivated());
            meter.setRef(meter.getRef() == null ? AppUtils.generateRef() : meter.getRef());
            meter.setCreatedAt(meter.getCreatedAt() == null ? Instant.now() : meter.getCreatedAt());
            meter.setUpdatedAt(meter.getUpdatedAt() == null ? Instant.now() : meter.getUpdatedAt());
            return meter;
        }
        throw new InvalidMeterTypeException(StringConstants.INVALID_METER_TYPE);
    }

    public static class CompositeMeterBundle {

        private String ref;

        private BigDecimal no;

        private boolean activated;

        private MeterType meterType;

        private Utility utility;

        private Subscriber subscriber;

        private Instant createdAt;

        private Instant updatedAt;

        public CompositeMeterBundle() {}

        public CompositeMeterBundle(String ref, BigDecimal no, boolean activated, MeterType meterType,
                                    Utility utility, Subscriber subscriber, Instant createdAt,
                                    Instant updatedAt) {
            setRef(ref);
            setNo(no);
            setActivated(activated);
            setMeterType(meterType);
            setUtility(utility);
            setSubscriber(subscriber);
            setCreatedAt(createdAt);
            setUpdatedAt(updatedAt);
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public BigDecimal getNo() {
            return no;
        }

        public void setNo(BigDecimal no) {
            this.no = no;
        }

        public boolean isActivated() {
            return activated;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }

        public MeterType getMeterType() {
            return meterType;
        }

        public void setMeterType(MeterType meterType) {
            this.meterType = meterType;
        }

        public Utility getUtility() {
            return utility;
        }

        public void setUtility(Utility utility) {
            this.utility = utility;
        }

        public Subscriber getSubscriber() {
            return subscriber;
        }

        public void setSubscriber(Subscriber subscriber) {
            this.subscriber = subscriber;
        }

        public Instant getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
        }

        public Instant getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

    public static class MeterBundle {

        @JsonProperty("no")
        private BigDecimal no;

        @JsonProperty("activated")
        private boolean activated;

        @JsonProperty("meter_type")
        private String meterType;

        @JsonProperty("utility_ref")
        private String utilityRef;

        @JsonProperty("subscriber_ref")
        private String subscriberRef;

        public MeterBundle(BigDecimal no, boolean activated, String meterType,
                           String utilityRef, String subscriberRef) {
            setNo(no);
            setActivated(activated);
            setMeterType(meterType);
            setSubscriberRef(subscriberRef);
            setUtilityRef(utilityRef);
        }

        public BigDecimal getNo() {
            return no;
        }

        public void setNo(BigDecimal no) {
            this.no = no;
        }

        public boolean getActivated() {
            return activated;
        }

        public void setActivated(Boolean activated) {
            this.activated = activated;
        }

        public String getMeterType() {
            return meterType;
        }

        public void setMeterType(String meterType) {
            this.meterType = meterType;
        }

        public String getUtilityRef() {
            return utilityRef;
        }

        public void setUtilityRef(String utilityRef) {
            this.utilityRef = utilityRef;
        }

        public String getSubscriberRef() {
            return subscriberRef;
        }

        public void setSubscriberRef(String subscriberRef) {
            this.subscriberRef = subscriberRef;
        }
    }
}
