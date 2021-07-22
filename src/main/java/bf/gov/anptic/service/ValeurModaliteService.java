package bf.gov.anptic.service;

import bf.gov.anptic.domain.ValeurModalite;
import bf.gov.anptic.repository.ValeurModaliteRepository;
import bf.gov.anptic.service.dto.PerformanceDTO;
import bf.gov.anptic.service.dto.ValeurModaliteDTO;
import bf.gov.anptic.service.mapper.ValeurModaliteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ValeurModalite}.
 */
@Service
@Transactional
public class ValeurModaliteService {

    private final Logger log = LoggerFactory.getLogger(ValeurModaliteService.class);

    private final ValeurModaliteRepository valeurModaliteRepository;

    private final ValeurModaliteMapper valeurModaliteMapper;

    public ValeurModaliteService(
        ValeurModaliteRepository valeurModaliteRepository,
        ValeurModaliteMapper valeurModaliteMapper) {
        this.valeurModaliteRepository = valeurModaliteRepository;
        this.valeurModaliteMapper = valeurModaliteMapper;
    }

    /**
     * Save a valeurModalite.
     *
     * @param valeurModaliteDTO the entity to save.
     * @return the persisted entity.
     */
    public ValeurModaliteDTO save(ValeurModaliteDTO valeurModaliteDTO) {
        log.debug("Request to save ValeurModalite : {}", valeurModaliteDTO);
        ValeurModalite valeurModalite = valeurModaliteMapper.toEntity(valeurModaliteDTO);
        valeurModalite = valeurModaliteRepository.save(valeurModalite);
        return valeurModaliteMapper.toDto(valeurModalite);
    }

    /**
     * Get all the valeurModalites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ValeurModaliteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ValeurModalites");
        return valeurModaliteRepository.findAll(pageable)
            .map(valeurModaliteMapper::toDto);
    }


    /**
     * Get one valeurModalite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ValeurModaliteDTO> findOne(Long id) {
        log.debug("Request to get ValeurModalite : {}", id);
        return valeurModaliteRepository.findById(id)
            .map(valeurModaliteMapper::toDto);
    }

    /**
     * Delete the valeurModalite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ValeurModalite : {}", id);
        valeurModaliteRepository.deleteById(id);
    }

    public Long deleteAll(List<ValeurModaliteDTO> valeurModalitesDTO) {
        log.debug("Request to delete ValeurModalite : {}", valeurModalitesDTO);
        List<ValeurModalite> valeurModalites = valeurModaliteMapper.toEntity(valeurModalitesDTO);
        if (!valeurModalites.isEmpty()) {
            valeurModaliteRepository.saveAll(valeurModalites);
        }
        return Long.parseLong("" + valeurModalites.size());
    }

    public Page<ValeurModaliteDTO> findAllModalitebyCommune(Long idExercice, Long idCommune, Pageable pageable) {
        if (idCommune != null) {
            return valeurModaliteRepository.findAllByExerciceIdAndCommuneId(pageable,idExercice, idCommune).map(valeurModaliteMapper::toDto);
        } else {
            return valeurModaliteRepository.findAllByExerciceId(pageable,idExercice).map(valeurModaliteMapper::toDto);
        }
    }

    public List<ValeurModaliteDTO> findModalitebyCommune(Long idExercice, Long idCommune) {
        List<ValeurModaliteDTO> valeurModalite = new ArrayList<>();
        if (idCommune != null) {
            /*valeurModalite = valeurModaliteRepository.findAll().stream().filter(val -> val.getExercice() != null &&
                val.getExercice().getId().equals(idExercice) && val.getCommune() != null
                && val.getCommune().getId().equals(idCommune)).map(valeurModaliteMapper::toDto)
                .collect(Collectors.toList());*/
            valeurModalite = valeurModaliteRepository.findAllByCommuneIdAndExerciceId(idCommune, idExercice)
                .stream().map(valeurModaliteMapper::toDto)
                .collect(Collectors.toList());
        } else {
            /*valeurModalite = valeurModaliteRepository.findAllByExerciceId(idExercice).stream().filter(val -> val.getExercice() != null &&
                val.getExercice().getId().equals(idExercice)).map(valeurModaliteMapper::toDto).collect(Collectors.toList());*/
            valeurModalite = valeurModaliteRepository.findAllByExerciceId(idExercice)
                .stream().map(valeurModaliteMapper::toDto).collect(Collectors.toList());
        }

        return valeurModalite;
    }

    public List<ValeurModaliteDTO> findAllValeurmodalite(Long idAnne, Long comId) {
        return valeurModaliteRepository.findValeurModalitesByCommuneIdAndExerciceId(comId,idAnne)
            .stream().map(valeurModaliteMapper::toDto).collect(Collectors.toList());
    }
}
