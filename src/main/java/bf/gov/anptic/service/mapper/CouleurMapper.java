package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.CouleurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Couleur} and its DTO {@link CouleurDTO}.
 */
@Mapper(componentModel = "spring", uses = {IndicateurMapper.class})
public interface CouleurMapper extends EntityMapper<CouleurDTO, Couleur> {

    @Mapping(source = "indicateur.id", target = "indicateurId")
    @Mapping(source = "indicateur.libelle", target = "libelleIndicateur")
    CouleurDTO toDto(Couleur couleur);

    @Mapping(source = "indicateurId", target = "indicateur")
    Couleur toEntity(CouleurDTO couleurDTO);

    default Couleur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Couleur couleur = new Couleur();
        couleur.setId(id);
        return couleur;
    }
}
