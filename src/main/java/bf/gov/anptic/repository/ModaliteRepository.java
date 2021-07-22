package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Modalite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Modalite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModaliteRepository extends JpaRepository<Modalite, Long> {
  Optional<Modalite> findDistinctFirstByLibelleAndDeletedIsFalse(String libelle);
  List<Modalite> findAllByIndicateurIdAndLibelleIsNullAndFormuleIsNullAndDeletedIsFalse(Long id);
  List<Modalite> findAllByIndicateurIdAndLibelleIsNotNullAndFormuleIsNullAndDeletedIsFalse(Long id);
  List<Modalite> findAllByIndicateurIdAndFormuleIsNotNullAndDeletedIsFalse(Long id);
  List<Modalite> findAllByIndicateurIdAndLibelleIsNotNullAndDeletedIsFalse(Long id);
  Optional<Modalite> findTop1ByLibelleAndDeletedIsFalse(String libelle);
  List<Modalite> findModalitesByIndicateurIdAndDeletedIsFalse(Long idIndic);
}
