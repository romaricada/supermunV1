package bf.gov.anptic.service;

import bf.gov.anptic.domain.EtatCommune;
import bf.gov.anptic.repository.EtatCommuneRepository;
import bf.gov.anptic.service.dto.EtatCommuneDTO;
import bf.gov.anptic.service.mapper.EtatCommuneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link EtatCommune}.
 */
@Service
@Transactional
public class EtatCommuneService {

    private final Logger log = LoggerFactory.getLogger(EtatCommuneService.class);

    private final EtatCommuneRepository etatCommuneRepository;

    private final EtatCommuneMapper etatCommuneMapper;

    public EtatCommuneService(EtatCommuneRepository etatCommuneRepository, EtatCommuneMapper etatCommuneMapper) {
        this.etatCommuneRepository = etatCommuneRepository;
        this.etatCommuneMapper = etatCommuneMapper;
    }

    /**
     * Save a etatCommune.
     *
     * @param etatCommuneDTO the entity to save.
     * @return the persisted entity.
     */
    public EtatCommuneDTO save(EtatCommuneDTO etatCommuneDTO) {
        log.debug("Request to save EtatCommune : {}", etatCommuneDTO);
        EtatCommune etatCommune = etatCommuneMapper.toEntity(etatCommuneDTO);
        etatCommune = etatCommuneRepository.save(etatCommune);
        return etatCommuneMapper.toDto(etatCommune);
    }

    /**
     * Get all the etatCommunes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EtatCommuneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EtatCommunes");
        return etatCommuneRepository.findAll(pageable)
            .map(etatCommuneMapper::toDto);
    }


    /**
     * Get one etatCommune by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EtatCommuneDTO> findOne(Long id) {
        log.debug("Request to get EtatCommune : {}", id);
        return etatCommuneRepository.findById(id)
            .map(etatCommuneMapper::toDto);
    }

    /**
     * Delete the etatCommune by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EtatCommune : {}", id);
        etatCommuneRepository.deleteById(id);
    }

    /**
     *
     * @param anneeId
     * @return
     */
    public Page<EtatCommuneDTO> getCommuneByExercice(Long anneeId, Pageable pageable) {
        return etatCommuneRepository.findAllByExerciceId(anneeId, pageable)
            .map(etatCommuneMapper::toDto);
    }

    public void removeAllCommuneFromYear(Long anneId) {
        List<EtatCommune> etatCommunes = etatCommuneRepository.findAllByExerciceId(anneId);
        if (!etatCommunes.isEmpty()) {
            etatCommuneRepository.deleteAll(etatCommunes);
        }
    }

    public Page<EtatCommuneDTO> getCommuneByRegionORProvince(Long anneeId, Long id, String criteria, Pageable pageable) {
        List<EtatCommuneDTO> etatCommuneDTOS1= new ArrayList<>();
        List<EtatCommuneDTO> etatCommuneDTOS = etatCommuneRepository.findAllByExerciceId(anneeId).stream()
            .map(etatCommuneMapper::toDto).collect(Collectors.toList());
        switch (criteria) {
            case "byRegion":{
                etatCommuneDTOS1 = etatCommuneDTOS.stream()
                    .filter(etatCommuneDTO -> etatCommuneDTO.getCommune().getRegionId().equals(id))
                    .sorted(Comparator.comparing(etatCommuneDTO -> etatCommuneDTO.getCommune().getLibelle()))
                    .collect(Collectors.toList());
                break; }

            case "byProvince":{
                etatCommuneDTOS1 = etatCommuneDTOS.stream()
                    .filter(etatCommuneDTO -> etatCommuneDTO.getCommune().getProvinceId().equals(id))
                    .sorted(Comparator.comparing(etatCommuneDTO -> etatCommuneDTO.getCommune().getLibelle()))
                    .collect(Collectors.toList());
                break; }
        }

        Page<EtatCommuneDTO> myPage = new PageImpl<>(etatCommuneDTOS1, pageable, etatCommuneDTOS1.size());
        return myPage;
    }


}
