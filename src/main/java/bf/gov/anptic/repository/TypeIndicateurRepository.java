package bf.gov.anptic.repository;
import bf.gov.anptic.domain.TypeIndicateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeIndicateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeIndicateurRepository extends JpaRepository<TypeIndicateur, Long> {

}
