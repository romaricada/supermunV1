package bf.gov.anptic.service;

import bf.gov.anptic.domain.Exercice;
import bf.gov.anptic.importation.service.ImportationFichierService;
import bf.gov.anptic.repository.ExerciceRepository;
import bf.gov.anptic.service.dto.ExerciceDTO;
import bf.gov.anptic.service.mapper.ExerciceMapper;
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
 * Service Implementation for managing {@link Exercice}.
 */
@Service
@Transactional
public class ExerciceService {

    private final Logger log = LoggerFactory.getLogger(ExerciceService.class);

    private final ExerciceRepository exerciceRepository;

    private final ExerciceMapper exerciceMapper;

    @Autowired
    ImportationFichierService importationFichierService;

    public ExerciceService(ExerciceRepository exerciceRepository, ExerciceMapper exerciceMapper) {
        this.exerciceRepository = exerciceRepository;
        this.exerciceMapper = exerciceMapper;
    }

    /**
     * Save a exercice.
     *
     * @param exerciceDTO the entity to save.
     * @return the persisted entity.
     */
    public ExerciceDTO save(ExerciceDTO exerciceDTO) {
        log.debug("Request to save Exercice : {}", exerciceDTO);
        Exercice exercice = exerciceMapper.toEntity(exerciceDTO);
        exercice = exerciceRepository.save(exercice);
        return exerciceMapper.toDto(exercice);
    }

    /**
     * Get all the exercices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExerciceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Exercices");
        List<ExerciceDTO> exercices =exerciceRepository.findAll().stream().filter(val-> !val.isDeleted())
            .sorted(Comparator.comparing(Exercice::getAnnee))
        .map(exerciceMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(exercices,pageable,exercices.size());
    }

    @Transactional(readOnly = true)
    public List<ExerciceDTO> findAllExercice() {
        log.debug("Request to get all exercice no page");
        return exerciceRepository.
            findAll().
            stream().filter(val ->val.isDeleted()!= null && !val.isDeleted() && val.getValidated())
            .sorted(Comparator.comparing(Exercice::getAnnee))
            .map(exerciceMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one exercice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExerciceDTO> findOne(Long id) {
        log.debug("Request to get Exercice : {}", id);
        return exerciceRepository.findById(id)
            .map(exerciceMapper::toDto);
    }

    /**
     * Delete the exercice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Exercice : {}", id);
        exerciceRepository.deleteById(id);
    }
    public Long deleteAll(List<ExerciceDTO> exercicesDTO) {
        log.debug("Request to delete Exercice : {}", exercicesDTO);
        List<Exercice> exercices = exerciceMapper.toEntity(exercicesDTO);
        if(!exercices.isEmpty()){
            exerciceRepository.saveAll(exercices);

        }
        return Long.parseLong(""+exercices.size());
    }
}
