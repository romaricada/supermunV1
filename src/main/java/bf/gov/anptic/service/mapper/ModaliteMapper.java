package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.ModaliteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Modalite} and its DTO {@link ModaliteDTO}.
 */
@Mapper(componentModel = "spring", uses = {IndicateurMapper.class})
public interface ModaliteMapper extends EntityMapper<ModaliteDTO, Modalite> {

    @Mapping(source = "indicateur.id", target = "indicateurId")
    @Mapping(source = "indicateur", target = "indicateur")
    ModaliteDTO toDto(Modalite modalite);

    @Mapping(source = "indicateurId", target = "indicateur")
    Modalite toEntity(ModaliteDTO modaliteDTO);

    default Modalite fromId(Long id) {
        if (id == null) {
            return null;
        }
        Modalite modalite = new Modalite();
        modalite.setId(id);
        return modalite;
    }
}
