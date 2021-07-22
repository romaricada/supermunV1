package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.ValeurModaliteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ValeurModalite} and its DTO {@link ValeurModaliteDTO}.
 */
@Mapper(componentModel = "spring", uses = {ExerciceMapper.class, CommuneMapper.class, ModaliteMapper.class})
public interface ValeurModaliteMapper extends EntityMapper<ValeurModaliteDTO, ValeurModalite> {

    @Mapping(source = "exercice.id", target = "exerciceId")
    @Mapping(source = "commune.id", target = "communeId")
    @Mapping(source = "modalite.id", target = "modaliteId")
    @Mapping(source = "modalite", target = "modalite")
    ValeurModaliteDTO toDto(ValeurModalite valeurModalite);

    @Mapping(source = "exerciceId", target = "exercice")
    @Mapping(source = "communeId", target = "commune")
    @Mapping(source = "modaliteId", target = "modalite")
    ValeurModalite toEntity(ValeurModaliteDTO valeurModaliteDTO);

    default ValeurModalite fromId(Long id) {
        if (id == null) {
            return null;
        }
        ValeurModalite valeurModalite = new ValeurModalite();
        valeurModalite.setId(id);
        return valeurModalite;
    }
}
