package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.PosterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Poster} and its DTO {@link PosterDTO}.
 */
@Mapper(componentModel = "spring", uses = {ExerciceMapper.class, CommuneMapper.class})
public interface PosterMapper extends EntityMapper<PosterDTO, Poster> {

    @Mapping(source = "exercice.id", target = "exerciceId")
    @Mapping(source = "exercice", target = "exercice")
    @Mapping(source = "commune.id", target = "communeId")
    @Mapping(source = "commune", target = "commune")
    PosterDTO toDto(Poster poster);

    @Mapping(source = "exerciceId", target = "exercice")
    @Mapping(source = "communeId", target = "commune")
    Poster toEntity(PosterDTO posterDTO);

    default Poster fromId(Long id) {
        if (id == null) {
            return null;
        }
        Poster poster = new Poster();
        poster.setId(id);
        return poster;
    }
}
