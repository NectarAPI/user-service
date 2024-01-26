package ke.co.nectar.user.service.subscriber;

import ke.co.nectar.user.entity.Subscriber;
import ke.co.nectar.user.service.subscriber.impl.SubscriberServiceImpl;

import java.util.List;

public interface SubscriberService {

    Subscriber add(SubscriberServiceImpl.SubscriberBundle subscriberBundle, String userRef) throws Exception;

    Subscriber find(String subscriberName) throws Exception;

    List<Subscriber> findAll() throws Exception;

    Subscriber findByRef(String ref) throws Exception;

    Subscriber update(SubscriberServiceImpl.SubscriberBundle subscriberBundle, String subscriberRef, String userRef) throws Exception;

    boolean activateSubscriber(String subscriberRef) throws Exception;

    boolean deactivateSubscriber(String subscriberRef) throws Exception;
}
