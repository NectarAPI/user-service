package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.notifications.NotificationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;

public interface NotificationUserRepository extends JpaRepository<NotificationUser, Long> {

    List<NotificationUser> findByUserRefAndReadFalse(String userRef);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("Update NotificationUser nu set nu.read = :status, nu.readDate = :readTimestamp where nu.notificationRef = :notificationRef and nu.userRef = :userRef")
    void setNotificationStatus(String userRef, String notificationRef,
                               boolean status, Instant readTimestamp) throws Exception;
}
