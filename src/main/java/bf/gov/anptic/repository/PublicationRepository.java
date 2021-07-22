package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;



/**
 * Spring Data  repository for the Publication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    Page<Publication> findAllByDeletedIsFalse(Pageable pageable);
}
