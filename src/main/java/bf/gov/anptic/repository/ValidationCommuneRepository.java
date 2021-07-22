package bf.gov.anptic.repository;

import bf.gov.anptic.domain.ValidationCommune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the EtatCommune entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidationCommuneRepository extends JpaRepository<ValidationCommune, Long> {
    ValidationCommune findTop1ByCommuneIdAndTypeDomaineIdAndExerciceId(Long comId, Long typeId, Long anneeId);
    List<ValidationCommune> findAllByTypeDomaineIdAndExerciceId(Long typeId, Long anneeId);
    boolean existsByCommuneIdAndTypeDomaineIdAndExerciceId(Long comId, Long typeId, Long anneeId);
}
