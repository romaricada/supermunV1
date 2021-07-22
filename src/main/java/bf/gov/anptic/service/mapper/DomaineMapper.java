package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.DomaineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Domaine} and its DTO {@link DomaineDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypeIndicateurMapper.class})
public interface DomaineMapper extends EntityMapper<DomaineDTO, Domaine> {

    @Mapping(source = "typeIndicateur.id", target = "typeIndicateurId")
    @Mapping(source = "typeIndicateur", target = "typeIndicateur")
    DomaineDTO toDto(Domaine domaine);

    @Mapping(source = "typeIndicateurId", target = "typeIndicateur")
    Domaine toEntity(DomaineDTO domaineDTO);

    default Domaine fromId(Long id) {
        if (id == null) {
            return null;
        }
        Domaine domaine = new Domaine();
        domaine.setId(id);
        return domaine;
    }
}
