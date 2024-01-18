package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.Utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Long> {

    Utility findByRef(String ref);

    Boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("Update Utility u set u.activated = false where u.ref=:ref and u.activated = true")
    int deactivateUtility(@Param("ref") String ref);

    @Transactional
    @Modifying
    @Query("Update Utility u set u.activated = true where u.ref=:ref and u.activated = false")
    int activateUtility(@Param("ref") String ref);

}
