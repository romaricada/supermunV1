package bf.gov.anptic.service;

import bf.gov.anptic.domain.ValidationCommune;
import bf.gov.anptic.repository.ValidationCommuneRepository;
import bf.gov.anptic.service.dto.ValidationCommuneDTO;
import bf.gov.anptic.service.mapper.ValidationCommuneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link bf.gov.anptic.domain.ValidationCommune}.
 */
@Service
@Transactional
public class ValidationCommuneService {

    private final Logger log = LoggerFactory.getLogger(ValidationCommuneService.class);

    private final ValidationCommuneRepository validationCommuneRepository;

    private final ValidationCommuneMapper validationCommuneMapper;

    public ValidationCommuneService(ValidationCommuneRepository validationCommuneRepository, ValidationCommuneMapper validationCommuneMapper) {
        this.validationCommuneMapper = validationCommuneMapper;
        this.validationCommuneRepository = validationCommuneRepository;
    }

    /**
     * Save a validationCommune.
     *
     * @param validationCommuneDTO the entity to save.
     * @return the persisted entity.
     */
    public ValidationCommuneDTO save(ValidationCommuneDTO validationCommuneDTO) {
        log.debug("Request to save ValidationCommune : {}", validationCommuneDTO);
        ValidationCommune validationCommune = validationCommuneMapper.toEntity(validationCommuneDTO);
        validationCommune = validationCommuneRepository.save(validationCommune);
        return validationCommuneMapper.toDto(validationCommune);
    }

    /**
     * Get all the validationCommunes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ValidationCommuneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ValidationCommunes");
        return validationCommuneRepository.findAll(pageable)
            .map(validationCommuneMapper::toDto);
    }


    /**
     * Get one validationCommune by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ValidationCommuneDTO> findOne(Long id) {
        log.debug("Request to get ValidationCommune : {}", id);
        return validationCommuneRepository.findById(id)
            .map(validationCommuneMapper::toDto);
    }

    /**
     * Delete the validationCommune by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ValidationCommune : {}", id);
        validationCommuneRepository.deleteById(id);
    }
}
