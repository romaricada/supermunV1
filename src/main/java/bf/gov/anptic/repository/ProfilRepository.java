package bf.gov.anptic.repository;
import bf.gov.anptic.domain.Profil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Profil entity.
 */
@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {

    @Query(value = "select distinct profil from Profil profil left join fetch profil.authorities",
        countQuery = "select count(distinct profil) from Profil profil")
    Page<Profil> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct profil from Profil profil left join fetch profil.authorities")
    List<Profil> findAllWithEagerRelationships();

    @Query("select profil from Profil profil left join fetch profil.authorities where profil.id =:id")
    Optional<Profil> findOneWithEagerRelationships(@Param("id") Long id);

}
