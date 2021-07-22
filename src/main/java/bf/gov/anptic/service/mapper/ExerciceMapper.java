package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.ExerciceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exercice} and its DTO {@link ExerciceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExerciceMapper extends EntityMapper<ExerciceDTO, Exercice> {



    default Exercice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Exercice exercice = new Exercice();
        exercice.setId(id);
        return exercice;
    }
}
