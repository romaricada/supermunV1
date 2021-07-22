package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Couleur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Couleur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CouleurRepository extends JpaRepository<Couleur, Long> {
    Page<Couleur> findAllByDeletedFalse(Pageable pageable);
    List<Couleur> findAllByDeletedFalse();
    List<Couleur> findAllByDeletedFalseAndIndicateurId(Long Id);
}
