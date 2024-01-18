package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.Meter;
import ke.co.nectar.user.entity.UtilityMeters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilityMetersRepository extends JpaRepository<UtilityMeters, Long> {

    UtilityMeters findByMeter(Meter meter) throws Exception;

}
