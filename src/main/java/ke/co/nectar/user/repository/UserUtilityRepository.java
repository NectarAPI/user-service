package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.UserUtility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserUtilityRepository extends JpaRepository<UserUtility, Long> {

    List<UserUtility> findByUserOrderByCreatedAtDesc(User user);
}
