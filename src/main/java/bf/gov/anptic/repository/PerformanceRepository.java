package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Performance;
import bf.gov.anptic.domain.enumeration.TypePerformance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Performance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    Optional<Performance> findTopByCommuneIdAndExerciceIdAndIndicateurIdAndDeletedIsFalse(Long idCom, Long idEx, Long idInd);
    Performance findTop1ByCommuneIdAndExerciceIdAndDomaineIdAndTypePerformanceAndDeletedIsFalse(Long idCom, Long idEx, Long idInd, TypePerformance typePerformance);
    List<Performance> findAllByDeletedIsFalseAndExerciceIdAndCommuneIdAndDomaineIdAndTypePerformance(Long idCom, Long idEx, Long idInd, TypePerformance typePerformance);
    Performance findTop1ByCommuneIdAndExerciceIdAndTypePerformanceAndTypeDomainIdAndDeletedIsFalse(Long idCom, Long idEx, TypePerformance typePerformance, Long typeId);
    Optional<Performance> findTop1ByDeletedIsFalseAndCommuneIdAndExerciceIdAndIndicateurIdAndTypePerformance(Long commune_id, Long exercice_id, Long indicateur_id, TypePerformance typePerformance);
    List<Performance> findPerformancesByExerciceIdAndDeletedIsFalse(Long idEx);
    List<Performance> findPerformancesByExerciceIdAndIndicateurIdAndDeletedIsFalse(Long idEx, Long idIndic);
    List<Performance> findAllByExerciceIdAndTypeDomainIdAndDeletedIsFalse(Long anneeId, Long typeId);
    List<Performance> findAllByCommuneIdAndExerciceIdAndDeletedIsFalse(Long comId, Long execId);

    List<Performance> findAllByDeletedIsFalseAndCommuneIdAndExerciceIdAndTypePerformance(Long comId, Long execId, TypePerformance typePerformance);
    List<Performance> findAllByCommuneIdAndExerciceIdAndIndicateurIdAndDeletedIsFalse(Long idCom, Long idEx, Long idIndic);

    boolean existsAllByExerciceIdAndTypeDomainId(Long id, Long typeId);
   }
