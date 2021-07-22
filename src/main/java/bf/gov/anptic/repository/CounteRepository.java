package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Counte;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Counte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CounteRepository extends JpaRepository<Counte, Long> {

}
