package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Dictionaires;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Dictionaires entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DictionairesRepository extends JpaRepository<Dictionaires, Long> {

}
