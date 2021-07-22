package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.ProvinceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Province} and its DTO {@link ProvinceDTO}.
 */
@Mapper(componentModel = "spring", uses = {RegionMapper.class})
public interface ProvinceMapper extends EntityMapper<ProvinceDTO, Province> {

    @Mapping(source = "region.id", target = "regionId")
    @Mapping(source = "region.libelle", target = "libelleRegion")
    ProvinceDTO toDto(Province province);

    @Mapping(source = "regionId", target = "region")
    Province toEntity(ProvinceDTO provinceDTO);

    default Province fromId(Long id) {
        if (id == null) {
            return null;
        }
        Province province = new Province();
        province.setId(id);
        return province;
    }
}
