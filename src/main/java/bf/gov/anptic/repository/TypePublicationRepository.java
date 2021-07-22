package bf.gov.anptic.repository;
import bf.gov.anptic.domain.TypePublication;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the TypePublication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypePublicationRepository extends JpaRepository<TypePublication, Long> {
}
