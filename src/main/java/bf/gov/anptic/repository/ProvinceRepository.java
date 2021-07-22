package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Province entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
Province findTop1ByLibelleAndDeletedIsFalse(String libelle);
}
