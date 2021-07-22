package bf.gov.anptic.repository;

import bf.gov.anptic.domain.ValeurModalite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ValeurModalite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValeurModaliteRepository extends JpaRepository<ValeurModalite, Long> {
    List<ValeurModalite> findAllByIndicateurIdAndExerciceIdAndCommuneId(Long indicId, Long exId, Long comId);
    ValeurModalite findTop1ByIndicateurIdAndExerciceIdAndCommuneId(Long indicId, Long exId, Long comId);
    ValeurModalite findTop1ByModaliteIdAndExerciceIdAndCommuneId(Long modId, Long exId, Long comId);
    Optional<ValeurModalite> findTop1ByModaliteIdAndCommuneIdAndExerciceId(Long modId, Long comId, Long exId);
    Optional<ValeurModalite> findTop1ByIndicateurIdAndCommuneIdAndExerciceId(Long indId, Long comId, Long exId);
    Page<ValeurModalite> findAllByExerciceIdAndCommuneId(Pageable pageable,Long idEx, Long idCom);
    Page<ValeurModalite> findAllByExerciceId(Pageable pageable,Long idEx);
    List<ValeurModalite> findValeurModalitesByCommuneIdAndExerciceId(Long idCom, Long idEx);
    List<ValeurModalite> findAllByCommuneIdAndExerciceId(Long idCom, Long idEx);
    List<ValeurModalite> findAllByExerciceId(Long anneeId);
}
