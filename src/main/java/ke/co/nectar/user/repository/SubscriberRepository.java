package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    Subscriber findByName(String name) throws Exception;

    Subscriber findByRef(String ref) throws Exception;

    @Transactional
    @Modifying
    @Query("Update Subscriber s set s.activated = true where s.ref=:ref and s.activated = false")
    int activateSubscriber(@Param("ref") String ref);

    @Transactional
    @Modifying
    @Query("Update Subscriber s set s.activated = false where s.ref=:ref and s.activated = true")
    int deactivateSubscriber(@Param("ref") String ref);

}
