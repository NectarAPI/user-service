package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.Subscriber;
import ke.co.nectar.user.entity.SubscriberUtility;
import ke.co.nectar.user.entity.Utility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberUtilityRepository extends JpaRepository<SubscriberUtility, Long> {

    SubscriberUtility findByUtility(Utility utility) throws Exception;

    SubscriberUtility findBySubscriberAndUtility(Subscriber subscriber, Utility utility) throws Exception;

}
