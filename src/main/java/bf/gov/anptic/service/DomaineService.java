package bf.gov.anptic.service;

import bf.gov.anptic.domain.Domaine;
import bf.gov.anptic.repository.DomaineRepository;
import bf.gov.anptic.service.dto.DomaineDTO;
import bf.gov.anptic.service.dto.DomainesDTO;
import bf.gov.anptic.service.mapper.DomaineMapper;
import bf.gov.anptic.service.mapper.DomainesMapper;
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
 * Service Implementation for managing {@link Domaine}.
 */
@Service
@Transactional
public class DomaineService {

    private final Logger log = LoggerFactory.getLogger(DomaineService.class);

    private final DomaineRepository domaineRepository;

    private final DomaineMapper domaineMapper;

    private final DomainesMapper domainesMapper;

    @Autowired
    PerformanceService performanceService;


    public DomaineService(DomaineRepository domaineRepository, DomaineMapper domaineMapper, DomainesMapper domainesMapper) {
        this.domaineRepository = domaineRepository;
        this.domaineMapper = domaineMapper;
        this.domainesMapper = domainesMapper;
    }

    /**
     * Save a domaine.
     *
     * @param domaineDTO the entity to save.
     * @return the persisted entity.
     */
    public DomaineDTO save(DomaineDTO domaineDTO) {
        log.debug("Request to save Domaine : {}", domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);
        domaine = domaineRepository.save(domaine);
        return domaineMapper.toDto(domaine);
    }

    /**
     * Get all the domaines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DomaineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Domaines");
        List<DomaineDTO> domaines =domaineRepository.findAll().stream().filter(val-> val.isDeleted() != null && !val.isDeleted())
        .sorted(Comparator.comparing(Domaine::getCode)).map(domaineMapper::toDto).collect(Collectors.toList());
        Page<DomaineDTO> page = new PageImpl<>(domaines,pageable,domaines.size());
        return page;
    }


    /**
     * Get one domaine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DomaineDTO> findOne(Long id) {
        log.debug("Request to get Domaine : {}", id);
        return domaineRepository.findById(id)
            .map(domaineMapper::toDto);
    }

    /**
     * Delete the domaine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Domaine : {}", id);
        domaineRepository.deleteById(id);
    }

    public Long deleteAll(List<DomaineDTO> domainesDTO) {
        log.debug("Request to delete Domaine : {}", domainesDTO);
        List<Domaine> domaines = domaineMapper.toEntity(domainesDTO);
        if (!domaines.isEmpty()) {
            domaineRepository.saveAll(domaines);
        }
        return Long.parseLong("" + domaines.size());
    }

    @Transactional(readOnly = true)
    public List<DomainesDTO> findAllDomaine() {
        return domaineRepository.findAllByDeletedIsFalse().stream().map(domainesMapper::toDto).collect(Collectors.toList());
    }

    public List<DomaineDTO> findAllDomaineAndIndicateur(Long idCommune, Long idExercice ,Long typeId) {
        log.debug("Request to calculte score of each domaine");
        List<DomaineDTO> domaines = domaineRepository.findAll().stream().filter(val -> val.getTypeIndicateur()!=null
            && val.getTypeIndicateur().getId().equals(typeId) && val.isDeleted() != null && !val.isDeleted())
            .sorted(Comparator.comparing(Domaine::getCode))
            .map(domaineMapper::toDto)
            .collect(Collectors.toList());
        domaines.forEach(domaineDTO -> {
            domaineDTO.setTotalScore(performanceService.getScoreOfComuneAndExercice(idCommune, idExercice, domaineDTO.getId()));
            domaineDTO.setIndicateurs(performanceService.findAllIndicateurByDomaineWithScore(idCommune, idExercice, domaineDTO.getId()));
            domaineDTO.setNbEtoile(performanceService.getStarByDomaine(idExercice,idCommune,domaineDTO.getId()));
        });
        return domaines;
    }


}
