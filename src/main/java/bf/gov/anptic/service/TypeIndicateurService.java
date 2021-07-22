package bf.gov.anptic.service;

import bf.gov.anptic.domain.TypeIndicateur;
import bf.gov.anptic.repository.TypeIndicateurRepository;
import bf.gov.anptic.service.dto.DomaineDTO;
import bf.gov.anptic.service.dto.TypeIndicateurDTO;
import bf.gov.anptic.service.mapper.TypeIndicateurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TypeIndicateur}.
 */
@Service
@Transactional
public class TypeIndicateurService {

    private final Logger log = LoggerFactory.getLogger(TypeIndicateurService.class);

    private final TypeIndicateurRepository typeIndicateurRepository;

    private final TypeIndicateurMapper typeIndicateurMapper;

    @Autowired
    private DomaineService domaineService;

    @Autowired
    PerformanceService performanceService;

    public TypeIndicateurService(TypeIndicateurRepository typeIndicateurRepository, TypeIndicateurMapper typeIndicateurMapper) {
        this.typeIndicateurRepository = typeIndicateurRepository;
        this.typeIndicateurMapper = typeIndicateurMapper;
    }

    /**
     * Save a typeIndicateur.
     *
     * @param typeIndicateurDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeIndicateurDTO save(TypeIndicateurDTO typeIndicateurDTO) {
        log.debug("Request to save TypeIndicateur : {}", typeIndicateurDTO);
        TypeIndicateur typeIndicateur = typeIndicateurMapper.toEntity(typeIndicateurDTO);
        typeIndicateur = typeIndicateurRepository.save(typeIndicateur);
        return typeIndicateurMapper.toDto(typeIndicateur);
    }

    /**
     * Get all the typeIndicateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeIndicateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeIndicateurs");
        List<TypeIndicateurDTO> typeindicateurs =typeIndicateurRepository.findAll().stream().filter(val-> val.isDeleted() != null && !val.isDeleted())
        .map(typeIndicateurMapper::toDto).collect(Collectors.toList());
        Page<TypeIndicateurDTO> page = new PageImpl<>(typeindicateurs,pageable,typeindicateurs.size());
        return page;
    }

    @Transactional(readOnly = true)
    public List<TypeIndicateurDTO> findAlltypeIndicateur() {
        log.debug("Request to get all type indicateurs no page");
        return typeIndicateurRepository.findAll().stream().filter(val -> val.isDeleted() != null && !val.isDeleted()).map(typeIndicateurMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one typeIndicateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeIndicateurDTO> findOne(Long id) {
        log.debug("Request to get TypeIndicateur : {}", id);
        return typeIndicateurRepository.findById(id)
            .map(typeIndicateurMapper::toDto);
    }

    /**
     * Delete the typeIndicateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeIndicateur : {}", id);
        typeIndicateurRepository.deleteById(id);
    }

    public Long deleteAll(List<TypeIndicateurDTO> typeIndicateursDTO) {
        log.debug("Request to delete TypeIndicateur : {}", typeIndicateursDTO);
        List<TypeIndicateur> typeIndicateurs = typeIndicateurMapper.toEntity(typeIndicateursDTO);
        if (!typeIndicateurs.isEmpty()) {
            typeIndicateurRepository.saveAll(typeIndicateurs);
        }
        return Long.parseLong("" + typeIndicateurs.size());
    }

    public List<TypeIndicateurDTO> findAllTypeIndicateurAndAllDommaineAndAllIndicateur(Long idCommune, Long idExercice) {
        List<TypeIndicateurDTO> indicateurDTOS = typeIndicateurRepository.findAll().stream().map(typeIndicateurMapper::toDto)
            .sorted(Comparator.comparing(TypeIndicateurDTO::getLibelle)).collect(Collectors.toList());
        indicateurDTOS.forEach(type -> {
            List<DomaineDTO> domaines = domaineService.findAllDomaineAndIndicateur(idCommune, idExercice, type.getId());
            type.setDomaines(domaines);
            type.setScoreTypeIndicateur(this.scoreCommuneByTypeIndicateur(domaines));
            type.setNombreEtoile(performanceService.getStartGlobalCommune(idExercice,idCommune,type.getId()));
        });
        return indicateurDTOS;
    }

    public Double scoreCommuneByTypeIndicateur(List<DomaineDTO> domaineDTOS) {
        if (!domaineDTOS.isEmpty()) {
            return domaineDTOS.stream().map(DomaineDTO::getTotalScore).mapToDouble(Double::doubleValue).sum();
        }
        return null;
    }

}
