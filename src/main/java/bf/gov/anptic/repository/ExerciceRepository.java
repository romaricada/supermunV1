package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Exercice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Exercice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciceRepository extends JpaRepository<Exercice, Long> {
    public Exercice findExerciceByAnnee(Integer annee);
}
