package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.UserActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserActivityCategoryRepository extends JpaRepository<UserActivityCategory, Long> {

    UserActivityCategory findByName(String name);
}
