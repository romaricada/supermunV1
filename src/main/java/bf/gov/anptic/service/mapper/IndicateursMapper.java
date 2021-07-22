package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.Indicateur;
import bf.gov.anptic.service.dto.IndicateurDTO;
import bf.gov.anptic.service.dto.IndicateursDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Indicateur} and its DTO {@link IndicateurDTO}.
 */
@Mapper(componentModel = "spring", uses = {DomaineMapper.class})
public interface IndicateursMapper extends EntityMapper<IndicateursDTO, Indicateur> {

    @Mapping(source = "domaine.id", target = "domaineId")
    @Mapping(source = "domaine", target = "domaine")
    IndicateursDTO toDto(Indicateur indicateur);

    @Mapping(source = "domaineId", target = "domaine")
    Indicateur toEntity(IndicateursDTO indicateurDTO);

    default Indicateur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Indicateur indicateur = new Indicateur();
        indicateur.setId(id);
        return indicateur;
    }
}
