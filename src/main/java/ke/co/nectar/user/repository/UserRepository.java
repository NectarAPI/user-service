package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.time.Instant;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String phoneNo);

    User findByUsername(String userName);

    User findByRef(String ref);

    User findByEmail(String email);

    User findByRefOrUsername(String ref, String username);

    User findByRefAndRememberToken(String ref, String rememberToken);

    @Transactional
    @Modifying
    @Query("Update User u set u.activated = false where u.ref=:ref and u.activated = true")
    int deactivateUser(@Param("ref") String ref);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("Update User u set u.rememberToken = :rememberToken where u.ref = :ref")
    int updateUserRememberToken(@Param("rememberToken") String rememberToken, @Param("ref") String ref);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("Update User u set u.emailVerifiedAt = :emailVerifiedAt where u.ref = :ref")
    int updateEmailVerifiedAt(@Param("emailVerifiedAt") Instant emailVerifiedAt, @Param("ref") String ref);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("Update User u set u.password = :password where u.ref = :ref")
    int updatePassword(@Param("password") String password, @Param("ref") String ref);
}
