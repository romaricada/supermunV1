package bf.gov.anptic.service.mapper;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.service.dto.PerformanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Performance} and its DTO {@link PerformanceDTO}.
 */
@Mapper(componentModel = "spring", uses = {CommuneMapper.class, IndicateurMapper.class, ExerciceMapper.class})
public interface PerformanceMapper extends EntityMapper<PerformanceDTO, Performance> {

    @Mapping(source = "commune.id", target = "communeId")
    @Mapping(source = "commune", target = "commune")
    @Mapping(source = "indicateur.id", target = "indicateurId")
    @Mapping(source = "indicateur", target = "indicateur")
    @Mapping(source = "indicateur.libelle", target = "indicateurLibelle")
    @Mapping(source = "indicateur.domaine.libelle", target = "domaineLibelle")
    @Mapping(source = "exercice.id", target = "exerciceId")
    @Mapping(source = "exercice", target = "exercice")
    PerformanceDTO toDto(Performance performance);

    @Mapping(source = "communeId", target = "commune")
    @Mapping(source = "indicateurId", target = "indicateur")
    @Mapping(source = "exerciceId", target = "exercice")
    Performance toEntity(PerformanceDTO performanceDTO);

    default Performance fromId(Long id) {
        if (id == null) {
            return null;
        }
        Performance performance = new Performance();
        performance.setId(id);
        return performance;
    }
}
