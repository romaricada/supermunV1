package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.IndicateurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Indicateur} and its DTO {@link IndicateurDTO}.
 */
@Mapper(componentModel = "spring", uses = {DomaineMapper.class})
public interface IndicateurMapper extends EntityMapper<IndicateurDTO, Indicateur> {

    @Mapping(source = "domaine.id", target = "domaineId")
    @Mapping(source = "domaine", target = "domaine")
    IndicateurDTO toDto(Indicateur indicateur);

    @Mapping(source = "domaineId", target = "domaine")
    Indicateur toEntity(IndicateurDTO indicateurDTO);

    default Indicateur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Indicateur indicateur = new Indicateur();
        indicateur.setId(id);
        return indicateur;
    }
}
