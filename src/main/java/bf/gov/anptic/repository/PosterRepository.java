package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Poster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Poster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PosterRepository extends JpaRepository<Poster, Long> {

}
