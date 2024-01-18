package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.Meter;
import ke.co.nectar.user.entity.Subscriber;
import ke.co.nectar.user.entity.SubscriberMeter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberMetersRepository extends JpaRepository<SubscriberMeter, Long> {

    SubscriberMeter findBySubscriberAndMeter(Subscriber subscriber, Meter meter) throws Exception;
}
