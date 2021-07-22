package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.ValidationCommune;
import bf.gov.anptic.service.dto.ValidationCommuneDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link ValidationCommune} and its DTO {@link ValidationCommuneDTO}.
 */
@Mapper(componentModel = "spring", uses = {CommuneMapper.class, ExerciceMapper.class})
public interface ValidationCommuneMapper extends EntityMapper<ValidationCommuneDTO, ValidationCommune> {

    @Mapping(source = "commune.id", target = "communeId")
    @Mapping(source = "commune.libelle", target = "communeLibelle")
    @Mapping(source = "exercice.id", target = "exerciceId")
    @Mapping(source = "exercice.annee", target = "exerciceAnnee")
    ValidationCommuneDTO toDto(ValidationCommune validationCommune);

    @Mapping(source = "communeId", target = "commune")
    @Mapping(source = "exerciceId", target = "exercice")
    ValidationCommune toEntity(ValidationCommuneDTO validationCommuneDTO);

    default ValidationCommune fromId(Long id) {
        if (id == null) {
            return null;
        }
        ValidationCommune validationCommune = new ValidationCommune();
        validationCommune.setId(id);
        return validationCommune;
    }
}
