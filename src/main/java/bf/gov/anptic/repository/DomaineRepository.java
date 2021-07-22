package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Domaine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Domaine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomaineRepository extends JpaRepository<Domaine, Long> {
    Optional<Domaine> findTop1ByLibelleAndDeletedIsFalse(String libelle);
    List<Domaine> findAllByTypeIndicateurIdAndDeletedIsFalse(Long id);
    List<Domaine> findAllByDeletedIsFalse();
}
