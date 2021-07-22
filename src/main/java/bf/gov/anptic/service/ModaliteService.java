package bf.gov.anptic.service;

import bf.gov.anptic.domain.Modalite;
import bf.gov.anptic.repository.ModaliteRepository;
import bf.gov.anptic.service.dto.ModaliteDTO;
import bf.gov.anptic.service.mapper.ModaliteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Service Implementation for managing {@link Modalite}.
 */
@Service
@Transactional
public class ModaliteService {

    private final Logger log = LoggerFactory.getLogger(ModaliteService.class);

    private final ModaliteRepository modaliteRepository;

    private final ModaliteMapper modaliteMapper;

    public ModaliteService(ModaliteRepository modaliteRepository, ModaliteMapper modaliteMapper) {
        this.modaliteRepository = modaliteRepository;
        this.modaliteMapper = modaliteMapper;
    }

    /**
     * Save a modalite.
     *
     * @param modaliteDTO the entity to save.
     * @return the persisted entity.
     */
    public ModaliteDTO save(ModaliteDTO modaliteDTO) {
        log.debug("Request to save Modalite : {}", modaliteDTO);
        Modalite modalite = modaliteMapper.toEntity(modaliteDTO);
        modalite = modaliteRepository.save(modalite);
        return modaliteMapper.toDto(modalite);
    }

    /**
     * Get all the modalites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ModaliteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Modalites");
        List<ModaliteDTO> modalites =modaliteRepository.findAll().stream().filter(val-> !val.isDeleted())
        .map(modaliteMapper::toDto).collect(Collectors.toList());
        Page<ModaliteDTO> page = new PageImpl<>(modalites,pageable,modalites.size());
        return page;
    }


    /**
     * Get one modalite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModaliteDTO> findOne(Long id) {
        log.debug("Request to get Modalite : {}", id);
        return modaliteRepository.findById(id)
            .map(modaliteMapper::toDto);
    }

    /**
     * Delete the modalite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Modalite : {}", id);
        modaliteRepository.deleteById(id);
    }
    public Long deleteAll(List<ModaliteDTO> modalitesDTO) {
        log.debug("Request to delete Modalite : {}", modalitesDTO);
        List<Modalite> modalites = modaliteMapper.toEntity(modalitesDTO);
        if(!modalites.isEmpty()){
            modaliteRepository.saveAll(modalites);
        }
        return Long.parseLong(""+modalites.size());
    }

    public List<Modalite> findAllModaliteByIndicateur(Long idIndicateur) {
        log.debug("Request to get all by indicateur where id = {}", idIndicateur);
        return modaliteRepository.findAll().stream()
            .filter(modalite ->  modalite.getIndicateur().getId().equals(idIndicateur))
            .sorted(Comparator.comparing(Modalite::getCode))
            .collect(Collectors.toList());
    }

    public List<ModaliteDTO> getModaliteByIndicateur(Long idIndicateur) {
        log.debug("Request to get all by indicateur where id = {}", idIndicateur);
        return modaliteRepository.findModalitesByIndicateurIdAndDeletedIsFalse(idIndicateur).stream().map(modaliteMapper::toDto).collect(Collectors.toList());
    }

}
