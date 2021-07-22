package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.CounteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Counte} and its DTO {@link CounteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CounteMapper extends EntityMapper<CounteDTO, Counte> {



    default Counte fromId(Long id) {
        if (id == null) {
            return null;
        }
        Counte counte = new Counte();
        counte.setId(id);
        return counte;
    }
}
