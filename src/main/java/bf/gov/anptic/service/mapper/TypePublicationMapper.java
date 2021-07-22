package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.TypePublicationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypePublication} and its DTO {@link TypePublicationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypePublicationMapper extends EntityMapper<TypePublicationDTO, TypePublication> {


    @Mapping(target = "publications", ignore = true)
    @Mapping(target = "removePublications", ignore = true)
    TypePublication toEntity(TypePublicationDTO typePublicationDTO);

    default TypePublication fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypePublication typePublication = new TypePublication();
        typePublication.setId(id);
        return typePublication;
    }
}
