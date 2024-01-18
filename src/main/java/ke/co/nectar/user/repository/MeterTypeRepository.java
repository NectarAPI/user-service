package ke.co.nectar.user.repository;

import ke.co.nectar.user.entity.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeterTypeRepository extends JpaRepository<MeterType, Long> {

    MeterType findByNameIgnoreCase(@Param("name") String name)  throws Exception;

    MeterType findByRef(String ref) throws Exception;
}
