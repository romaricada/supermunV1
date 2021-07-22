package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.Domaine;
import bf.gov.anptic.service.dto.DomaineDTO;
import bf.gov.anptic.service.dto.DomainesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Domaine} and its DTO {@link DomaineDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypeIndicateurMapper.class})
public interface DomainesMapper extends EntityMapper<DomainesDTO, Domaine> {

    @Mapping(source = "typeIndicateur.id", target = "typeIndicateurId")
    @Mapping(source = "typeIndicateur", target = "typeIndicateur")
    DomainesDTO toDto(Domaine domaine);

    @Mapping(source = "typeIndicateurId", target = "typeIndicateur")
    Domaine toEntity(DomainesDTO domaineDTO);

    default Domaine fromId(Long id) {
        if (id == null) {
            return null;
        }
        Domaine domaine = new Domaine();
        domaine.setId(id);
        return domaine;
    }
}
