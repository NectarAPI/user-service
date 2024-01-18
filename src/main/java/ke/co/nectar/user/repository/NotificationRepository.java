package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findByRef(@Param("ref") String notificationRef);
}
