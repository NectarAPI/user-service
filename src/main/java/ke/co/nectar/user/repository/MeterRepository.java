package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {

    Meter findByRef(String meterRef) throws Exception;

    boolean existsByNo(BigDecimal no) throws Exception;

    @Transactional
    @Modifying
    @Query("Update Meter m set m.activated = false where m.ref=:ref and m.activated = true")
    int deactivateMeter(@Param("ref") String ref);

    @Transactional
    @Modifying
    @Query("Update Meter m set m.activated = true where m.ref=:ref and m.activated = false")
    int activateMeter(@Param("ref") String ref);

}
