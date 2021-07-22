package bf.gov.anptic.repository;
import bf.gov.anptic.domain.EtatCommune;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the EtatCommune entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtatCommuneRepository extends JpaRepository<EtatCommune, Long> {
    Page<EtatCommune> findAllByExerciceId(Long execId, Pageable pageable);
    List<EtatCommune> findAllByExerciceId(Long execId);
    EtatCommune findTop1ByExerciceIdAndCommuneId(Long execId, Long comId);
    Long countAllByExerciceId(Long anneeId);
    boolean existsByExerciceId(Long id);
    boolean existsByExerciceIdAndAndCommuneId(Long id, Long comId);
}
