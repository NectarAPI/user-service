package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    List<UserActivityLog> findByUserOrderByCreatedAtDesc(User user);
}
