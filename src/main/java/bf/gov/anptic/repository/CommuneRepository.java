package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Commune;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Commune entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {

    @Query(value = "SELECT cast(geom as varchar) geom FROM commune_geojson", nativeQuery = true)
    Object getAllConvertToGeoJSON();

    Optional<Commune> findDistinctFirstByLibelleAndDeletedIsFalse(String libelle);
    Commune findTop1ByLibelleAndDeletedIsFalse(String libelle);
    Page<Commune> findCommunesByDeletedIsFalse(Pageable pageable);
    List<Commune> findAllByDeletedIsFalse();
    List<Commune> findAllByLibelleAndDeletedIsFalse(String libelle);
}
