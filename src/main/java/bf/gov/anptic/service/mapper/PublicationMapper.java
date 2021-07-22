package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.PublicationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Publication} and its DTO {@link PublicationDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypePublicationMapper.class})
public interface PublicationMapper extends EntityMapper<PublicationDTO, Publication> {

    @Mapping(source = "typePublication.id", target = "typePublicationId")
    @Mapping(source = "typePublication.libelle", target = "typePublicationLibelle")
    PublicationDTO toDto(Publication publication);

    @Mapping(source = "typePublicationId", target = "typePublication")
    Publication toEntity(PublicationDTO publicationDTO);

    default Publication fromId(Long id) {
        if (id == null) {
            return null;
        }
        Publication publication = new Publication();
        publication.setId(id);
        return publication;
    }
}
