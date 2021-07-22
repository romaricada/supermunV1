package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.EtatCommuneDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EtatCommune} and its DTO {@link EtatCommuneDTO}.
 */
@Mapper(componentModel = "spring", uses = {CommuneMapper.class, ExerciceMapper.class})
public interface EtatCommuneMapper extends EntityMapper<EtatCommuneDTO, EtatCommune> {

    @Mapping(source = "commune.id", target = "communeId")
    @Mapping(source = "commune.libelle", target = "communeLibelle")
    @Mapping(source = "exercice.id", target = "exerciceId")
    @Mapping(source = "exercice.annee", target = "exerciceAnnee")
    EtatCommuneDTO toDto(EtatCommune etatCommune);

    @Mapping(source = "communeId", target = "commune")
    @Mapping(source = "exerciceId", target = "exercice")
    EtatCommune toEntity(EtatCommuneDTO etatCommuneDTO);

    default EtatCommune fromId(Long id) {
        if (id == null) {
            return null;
        }
        EtatCommune etatCommune = new EtatCommune();
        etatCommune.setId(id);
        return etatCommune;
    }
}
