package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.Commune;
import bf.gov.anptic.service.dto.CommuneDTO;
import bf.gov.anptic.service.dto.CommunesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Commune} and its DTO {@link CommuneDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProvinceMapper.class})
public interface CommunesMapper extends EntityMapper<CommunesDTO, Commune> {

    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "province.libelle", target = "libelleProvince")
    @Mapping(source = "province.region.id", target = "regionId")
    @Mapping(source = "province.region.libelle", target = "libelleRegion")
    CommunesDTO toDto(Commune commune);

    @Mapping(source = "provinceId", target = "province")
    Commune toEntity(CommunesDTO communesDTO);

    default Commune fromId(Long id) {
        if (id == null) {
            return null;
        }
        Commune commune = new Commune();
        commune.setId(id);
        return commune;
    }
}
