package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.TypeIndicateurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeIndicateur} and its DTO {@link TypeIndicateurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeIndicateurMapper extends EntityMapper<TypeIndicateurDTO, TypeIndicateur> {



    default TypeIndicateur fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeIndicateur typeIndicateur = new TypeIndicateur();
        typeIndicateur.setId(id);
        return typeIndicateur;
    }
}
