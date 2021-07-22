package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Indicateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Indicateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndicateurRepository extends JpaRepository<Indicateur, Long> {
 Optional<Indicateur> findDistinctFirstByLibelleAndDeletedIsFalse(String libelle);
 Optional<Indicateur> findTop1ByLibelleAndDeletedIsFalse(String libelle);
List<Indicateur> findAllByDeletedIsFalse();
 List<Indicateur> findAllByDomaineIdAndDeletedIsFalse(Long id);
}
