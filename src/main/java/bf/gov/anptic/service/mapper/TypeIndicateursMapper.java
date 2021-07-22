package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.TypeIndicateur;
import bf.gov.anptic.service.dto.TypeIndicateurDTO;
import bf.gov.anptic.service.dto.TypeIndicateursDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link TypeIndicateur} and its DTO {@link TypeIndicateurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeIndicateursMapper extends EntityMapper<TypeIndicateursDTO, TypeIndicateur> {



    default TypeIndicateur fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeIndicateur typeIndicateur = new TypeIndicateur();
        typeIndicateur.setId(id);
        return typeIndicateur;
    }
}
