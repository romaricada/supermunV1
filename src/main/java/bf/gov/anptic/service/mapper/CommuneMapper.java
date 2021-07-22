package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.CommuneDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commune} and its DTO {@link CommuneDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProvinceMapper.class})
public interface CommuneMapper extends EntityMapper<CommuneDTO, Commune> {

    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "province.libelle", target = "libelleProvince")
    @Mapping(source = "province.region.id", target = "regionId")
    @Mapping(source = "province.region.libelle", target = "libelleRegion")
    CommuneDTO toDto(Commune commune);

    @Mapping(source = "provinceId", target = "province")
    Commune toEntity(CommuneDTO communeDTO);

    default Commune fromId(Long id) {
        if (id == null) {
            return null;
        }
        Commune commune = new Commune();
        commune.setId(id);
        return commune;
    }
}
