package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Region;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Region entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findAllByDeletedIsFalse();

}
