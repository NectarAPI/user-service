package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Credentials findByRef(String ref);

    Credentials findByKey(String key);

    @Query("Select credentials from Credentials credentials LEFT JOIN credentials.user user where user.ref=:ref")
    List<Credentials> findByUserRef(@Param("ref") String ref);

    @Transactional
    @Modifying
    @Query("Update Credentials credentials set credentials.activated=false, updatedAt=instant where credentials.ref=:ref")
    int deactivateCredentials(@Param("ref") String ref);

    @Transactional
    @Modifying
    @Query("Update Credentials credentials set credentials.activated=true, updatedAt=instant where credentials.ref=:ref")
    int activateCredentials(@Param("ref") String ref);
}
